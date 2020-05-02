package com.foi_bois.zisprojekt.model;

public class User {
    //private String id; sifra, i sve ostalo je u FirebaseUser!
    private String avatar; //trebala base64 bit, ipak je sad link na firestore
    private String username;
    private Location location;
    private long locationUpdated; //ServerValue.TIMESTAMP daje u ms razliku od UNIX vremena

    public User(){ //za JSON to POJO

    }

    public User( String username, String avatar, Location location, long locationUpdated){
        this.username = username;
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

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    // public Date getLocationUpdated(){ return locationUpdated; }

   // public void setLocationUpdated(Date date) { this.locationUpdated = date; }

}
