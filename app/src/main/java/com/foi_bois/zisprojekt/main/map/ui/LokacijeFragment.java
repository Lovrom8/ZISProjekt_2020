package com.foi_bois.zisprojekt.main.map.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.foi_bois.zisprojekt.R;
import com.foi_bois.zisprojekt.base.BaseFragment;
import com.foi_bois.zisprojekt.firebase.BazaHelper;
import com.foi_bois.zisprojekt.lib.Helper;
import com.foi_bois.zisprojekt.lib.PermsHelper;
import com.foi_bois.zisprojekt.main.map.LokacijePresenter;
import com.foi_bois.zisprojekt.model.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import javax.inject.Inject;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LokacijeFragment extends BaseFragment implements LokacijeView, OnMapReadyCallback {
    @Inject
    LokacijePresenter<LokacijeView> presenter;

    private GoogleMap map;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Marker mojaPozicija, testMark1, testMark2;
    private LocationRequest mLocationRequest;
    private BazaHelper baza;

    private long INTERVAL = 10 * 1000;
    private long NAJBRZI_INTERVAL = 10 * 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        presenter.attach(this);

        View inf = inflater.inflate(R.layout.fragment_tab_main, container, false); //VAŽNO: prvo inflate, tek onda findFragmentById

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        presenter.attach(this);
        baza = baza.getInstance();

        return inf;
    }

    @Override
    public void onDestroy(){ //izgleda da nije protected onDestroy kod fragmenata :(
        presenter.detach();
        super.onDestroy();
    }

    private void naPromjeniLokacije(android.location.Location location) {
        String msg = "Nova trenutna lokacija: " + location.getLatitude() + ","
                + location.getLongitude();
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(mojaPozicija != null) { //JIC
            mojaPozicija.setPosition(latLng);
            baza.osvjeziMojuLokaciju(getActivity(), new Location(latLng));
        }
    }

   public void onLocationsRecieved(){

   }

   private void loadOtherLocations(){
       map.clear(); //optimizacija lvl 100 :)

       baza.procitajPodatke(new BazaHelper.FirebaseCallback() {
           @Override
           public void onCallback( Map<String, Location> lokacije) {
               String mojId = FirebaseAuth.getInstance().getCurrentUser().getUid();

               for(String id : baza.lokacije.keySet()){
                   if(id.equals(mojId))
                       continue;

                   Location location = baza.lokacije.get(id);
                   map.addMarker(new MarkerOptions()
                           .position(new LatLng(location.getLat(), location.getLng())).title("test"));
               }
           }
       });

       addMyLocation();
   }

    private boolean checkLocationPerms() {
       return PermsHelper.checkPerms(getActivity(), ACCESS_FINE_LOCATION);
    }

    private void createTestMarkers(boolean dodajOstale) {
        LatLng yourPos = new LatLng(45.8150, 15.9819); //Zagreb
        LatLng testPos = new LatLng(48.2082, 16.3738); //Beč
        LatLng testPos2 = new LatLng(48.8566, 2.3522); //Paris

        mojaPozicija = map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(yourPos).title("Tvoja pozicija")); //stavi fancy boju za sebe D:
        map.moveCamera(CameraUpdateFactory.newLatLng(yourPos));

        if(!dodajOstale)
            return;

        testMark1 = map.addMarker(new MarkerOptions()
                .position(testPos).title("Druga pozicija"));
        map.moveCamera(CameraUpdateFactory.newLatLng(testPos));

        testMark2 = map.addMarker(new MarkerOptions()
                .position(testPos2).title("Treca pozicija"));
        map.moveCamera(CameraUpdateFactory.newLatLng(testPos2));
    }

    protected void startLocationUpdate() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
                        naPromjeniLokacije(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    @Override
    public void addMyLocation(){
        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(mojaPozicija.getPosition()).title("Tvoja pozicija"));
    }

    private void setSettings() {
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.equals(mojaPozicija)) //nema potrebe da racunamo udaljenost do nase pozicije
                    return false;

                marker.setTitle("Udaljenost " + Helper.calculateDistance(marker, mojaPozicija));
                marker.showInfoWindow();

                return true; //iz nekog razloga mora ovo bit bool
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if(checkLocationPerms()) {
            googleMap.setMyLocationEnabled(true);
        }

        setSettings();

        //if(BuildConfig.DEBUG) //ako smo u razvojnom modu
        createTestMarkers(false);

        startLocationUpdate();
       // loadOtherLocations();
    }
}
