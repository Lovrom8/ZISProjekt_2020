package com.foi_bois.zisprojekt;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

public class TabMain extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Marker mojaPozicija, testMark1, testMark2;
    private LocationRequest mLocationRequest;
    private Baza baza;

    private long INTERVAL = 10 * 1000;
    private long NAJBRZI_INTERVAL = 10 * 1000;

    public TabMain() { }

    private void dodajMojuLokaciju(){
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(mojaPozicija.getPosition()).title("Tvoja pozicija"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inf = inflater.inflate(R.layout.fragment_tab_main, container, false); //VAŽNO: prvo inflate, tek onda findFragmentById

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        baza = new Baza();
   /*
     mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mojaPozicija.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                        }
                    }
                }); */
         return inf;
    }


    protected void pokreniAzuriranjeLokacije() {
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

    private void naPromjeniLokacije(Location location) {
        String msg = "Nova trenutna lokacija: " + location.getLatitude() + ","
                + location.getLongitude();
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(mojaPozicija != null) { //JIC
            mojaPozicija.setPosition(latLng);
            baza.osvjeziMojuLokaciju(getActivity(), new Lokacija(latLng));
        }
    }

    private void ucitajDrugePozicije(){
        mMap.clear(); //optimizacija lvl 100 :)

        baza.procitajPodatke(new Baza.FirebaseCallback() {
            @Override
            public void onCallback( Map<String, Lokacija> lokacije) {
                String mojId = Helper.id(getActivity());

                for(String id : baza.lokacije.keySet()){
                    if(id.equals(mojId))
                        continue;

                   mMap.addMarker(new MarkerOptions()
                            .position(baza.lokacije.get(id).getLocation()).title("test"));
                }
            }
        });

        dodajMojuLokaciju();
    }

    //jer je async, moramo citati uz callback

    private void stvoriTestneTocke() {
        LatLng yourPos = new LatLng(45.8150, 15.9819); //Zagreb
        LatLng testPos = new LatLng(48.2082, 16.3738); //Beč
        LatLng testPos2 = new LatLng(48.8566, 2.3522); //Paris

        mojaPozicija = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(yourPos).title("Tvoja pozicija")); //stavi fancy boju za sebe D:
        mMap.moveCamera(CameraUpdateFactory.newLatLng(yourPos));

       /* testMark1 = mMap.addMarker(new MarkerOptions()
                .position(testPos).title("Druga pozicija"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(testPos));

        testMark2 = mMap.addMarker(new MarkerOptions()
                .position(testPos2).title("Treca pozicija"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(testPos2));

        baza = new Baza();
        */

        //baza.OsvjeziMojuLokaciju(getContext(), new Lokacija(mojaPozicija.getPosition()));
        //Lokacija lokacija1 = new Lokacija(testMark1.getPosition()); //stavi u bazu testne podatke
       // Lokacija lokacija2 = new Lokacija(testMark2.getPosition());
        //baza.OsvjeziLokaciju(getContext(), lokacija1, "234212434343");
      //  baza.OsvjeziLokaciju(getContext(), lokacija2, "200554054054");
    }

    private void postaviPostavke() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.equals(mojaPozicija)) //nema potrebe da racunamo udaljenost do nase pozicije
                    return false;

                marker.setTitle("Udaljenost " + Helper.izracunajUdaljenost(marker, mojaPozicija));
                marker.showInfoWindow();

                return true; //iz nekog razloga mora ovo bit bool
            }
        });
    }

    private boolean provjeriDozvole() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            zahtjevajDozvole();
            return false;
        }
    }

    private void zahtjevajDozvole() {
        int REQUEST_LOCATION = 0;
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(provjeriDozvole()) {
            googleMap.setMyLocationEnabled(true);
        }

        postaviPostavke();

        //if(BuildConfig.DEBUG) //ako smo u razvojnom modu
        stvoriTestneTocke();

        pokreniAzuriranjeLokacije();
        ucitajDrugePozicije();
    }
}

