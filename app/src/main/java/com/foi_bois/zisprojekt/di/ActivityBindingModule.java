package com.foi_bois.zisprojekt.di;

import com.foi_bois.zisprojekt.di.auth.LoginActivityModule;
import com.foi_bois.zisprojekt.di.auth.SignupActivityModule;
import com.foi_bois.zisprojekt.auth.ui.LoginActivity;
import com.foi_bois.zisprojekt.auth.ui.SignupActivity;
import com.foi_bois.zisprojekt.main.MainActivity;
import com.foi_bois.zisprojekt.di.main.MainActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = LoginActivityModule.class) //sve aktivnosti
    abstract LoginActivity loginActivity();

    @ContributesAndroidInjector(modules = SignupActivityModule.class)
    abstract SignupActivity signupActivity();

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity mainActivity();
}