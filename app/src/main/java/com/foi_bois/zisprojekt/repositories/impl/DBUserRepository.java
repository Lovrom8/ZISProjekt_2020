package com.foi_bois.zisprojekt.repositories.impl;

import android.content.Context;

import com.foi_bois.zisprojekt.User;
import com.foi_bois.zisprojekt.repositories.UserRepository;

public class DBUserRepository implements UserRepository {

    public DBUserRepository(final Context ctx){

    }

    @Override
    public User getCurrentUser(){
        //TODO: fake user ofc
       // return new User("123", "234");
        return null;
    }
}
