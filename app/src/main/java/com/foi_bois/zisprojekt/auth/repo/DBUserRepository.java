package com.foi_bois.zisprojekt.auth.repo;

import android.content.Context;

import com.foi_bois.zisprojekt.model.User;

public class DBUserRepository implements UserRepository {

    public DBUserRepository(final Context ctx){

    }

    @Override
    public User getCurrentUser(){
        //TODO: fake user ofc
       // return new User("123", "234");
        //throw new NumberFormatException();
        return null;
    }
}
