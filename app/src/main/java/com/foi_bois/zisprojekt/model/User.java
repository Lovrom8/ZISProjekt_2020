package com.foi_bois.zisprojekt.model;

import java.util.Date;

public class User {
    //private String id; sifra, i sve ostalo je u FirebaseUser!
    private String avatar; //trebala base64 bit, sad je link na firestore
    private Location location;
    private long locationUpdated; //ServerValue.TIMESTAMP daje u ms razliku od UNIX vremena

    public User(){ //za JSON to POJO

    }

    public User( String avatar, Location location, long locationUpdated){
        this.avatar = avatar;
        this.location = location;
        this.locationUpdated = locationUpdated;
    }

    public void setAvatar(String img){
        this.avatar = img;
    }

    public String getAvatar(){
        return avatar;
    }

    public Location getLocation() { return location; }

    public void setLocation(Location location) { this.location = location; }

    public long getLocationUpdated() { return locationUpdated; }

    public void setLocationUpdated(long locationUpdated) { this.locationUpdated = locationUpdated; }

    // public Date getLocationUpdated(){ return locationUpdated; }

   // public void setLocationUpdated(Date date) { this.locationUpdated = date; }

}
