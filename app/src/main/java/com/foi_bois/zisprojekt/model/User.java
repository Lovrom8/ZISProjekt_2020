package com.foi_bois.zisprojekt.model;

public class User {
    private String name;
    private String id;
    private String avatar; //base64 enc slika
    private Lokacija lokacija;

    public User(String name, String id, String avatar){
        this.name = name;
        this.id = id;
        this.avatar = avatar;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setAvatar(String img){
        this.avatar = img;
    }

    public String getAvatar(){
        return avatar;
    }

    public Lokacija getLokacija() { return lokacija; }

    public void setLokacija(Lokacija lokacija) { this.lokacija = lokacija; }
}
