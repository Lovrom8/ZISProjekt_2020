package com.foi_bois.zisprojekt.main.map.ui;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.foi_bois.zisprojekt.R;
import com.foi_bois.zisprojekt.base.BaseFragment;
import com.foi_bois.zisprojekt.firebase.BazaHelper;
import com.foi_bois.zisprojekt.lib.Helper;
import com.foi_bois.zisprojekt.lib.PermsHelper;
import com.foi_bois.zisprojekt.main.map.LokacijePresenter;
import com.foi_bois.zisprojekt.model.Location;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LokacijeFragment extends BaseFragment implements LokacijeView, OnMapReadyCallback {
    @Inject
    LokacijePresenter<LokacijeView> presenter;

    private final String TAG = "Locations";
    private GoogleMap map;
    private Marker myPosMarker;
    private LatLng oldLocation;
    private LocationRequest mLocationRequest;
    private BazaHelper baza;

    private final long INTERVAL = 5 * 1000;
    private final long NAJBRZI_INTERVAL = 5 * 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        presenter.attach(this);

        View inf = inflater.inflate(R.layout.fragment_tab_main, container, false); //VAŽNO: prvo inflate, tek onda findFragmentById

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        oldLocation = new LatLng(0.0,0.0);
        presenter.attach(this);

        return inf;
    }

    @Override
    public void onDestroy(){ //izgleda da nije protected onDestroy kod fragmenata :(
        presenter.detach();
        super.onDestroy();
    }

    private void onLocationChanged(android.location.Location newLocation) {
        if(oldLocation.latitude == newLocation.getLatitude() && oldLocation.longitude == newLocation.getLongitude() )
            return;

        String msg = getResources().getString(R.string.toast_new_location) + " : " +
                newLocation.getLatitude() + " , " + newLocation.getLongitude();
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

        LatLng newPos = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
        if(myPosMarker != null) { //JIC
            myPosMarker.setPosition(newPos);

            BazaHelper.getInstance().refreshMyLocation(new Location(newPos), new BazaHelper.FirebaseUserCallback() {
                @Override
                public void onCallback(boolean isSuccesful) {
                    if(isSuccesful){
                        Log.d(TAG, getResources().getString(R.string.log_locationSavingSuccess));
                    }else{
                        Log.d(TAG, getResources().getString(R.string.log_locationSavingFail));
                    }
                }
            });
        }

        oldLocation = newPos;
    }

    public void clearMap(){
        map.clear();
    }

    private void startLocationUpdate() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(NAJBRZI_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    @Override
    public void addMyLocation(LatLng myPos){
        myPosMarker = map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(myPos).title("Tvoja pozicija"));

        map.moveCamera(CameraUpdateFactory.newLatLng(myPos));
    }

    @Override
    public void addPositionMarker(LatLng pos, String title){
        map.addMarker(new MarkerOptions()
                .position(pos).title(title));
    }

    private void setSettings() {
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) { //iz nekog razloga mora ovo bit bool
                if(marker.equals(myPosMarker)) //nema potrebe da racunamo udaljenost do nase pozicije
                    return false;

                marker.setTitle("Udaljenost " + Helper.calculateDistance(marker, myPosMarker));
                marker.showInfoWindow();

                return true;
            }
        });
    }

    private boolean checkLocationPerms() {
        return PermsHelper.checkPerms(getActivity(), ACCESS_FINE_LOCATION);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if(checkLocationPerms()) {
            googleMap.setMyLocationEnabled(true);
        }

        setSettings();

        //if(BuildConfig.DEBUG) //ako smo u razvojnom modu
        //presenter.createTestMarkers(true);

        startLocationUpdate();
        presenter.loadOtherLocations(); //SREDI procitajPodatke() hvata onDataChange, tak da ovo samo jednom zovemo
    }
}
