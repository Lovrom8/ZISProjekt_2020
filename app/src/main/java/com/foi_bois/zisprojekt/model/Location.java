package com.foi_bois.zisprojekt.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

public class Location {
    private double lat;
    private double lng;

    public Location() {
    } //treba i jedan prazan konstruktor za firebase

    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Location(LatLng latlng) {
        this.lat = latlng.latitude;
        this.lng = latlng.longitude;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Exclude
    public LatLng getLatLng(){ return new LatLng(lat, lng); }
}



