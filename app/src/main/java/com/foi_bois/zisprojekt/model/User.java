package com.foi_bois.zisprojekt.model;

public class User {
    String name;
    String id;
    String avatar; //base64 enc slika

    public User(String name, String id){
        this.name = name;
        this.id = id;
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
}
