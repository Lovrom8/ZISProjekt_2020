package com.foi_bois.zisprojekt;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class Lokacija {
    //private String _id;
    private LatLng _lokacija;

    //private double _lat, _lng;
    Lokacija(){ //treba i jedan prazan konstruktor za firebase
    }

        Lokacija(double lat, double lng){
            _lokacija = new LatLng(lat, lng);
        }

        Lokacija(LatLng lokacija){
            _lokacija = lokacija;
    }

    public void setLocation(LatLng lokacija){
        _lokacija = lokacija;
    }

    public LatLng getLocation(){
        return _lokacija;
    }
}
