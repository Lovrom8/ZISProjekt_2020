package com.foi_bois.zisprojekt.model;

import com.google.android.gms.maps.model.LatLng;

public class Lokacija {
    private LatLng _lokacija;

    public Lokacija(){} //treba i jedan prazan konstruktor za firebase

    public Lokacija(double lat, double lng){
            _lokacija = new LatLng(lat, lng);
    }

    public Lokacija(LatLng lokacija){
            _lokacija = lokacija;
    }

    public void setLocation(LatLng lokacija){
        _lokacija = lokacija;
    }

    public LatLng getLocation(){
        return _lokacija;
    }
}
