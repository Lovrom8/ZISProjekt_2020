package com.foi_bois.zisprojekt.di;

import com.foi_bois.zisprojekt.auth.LoginActivityModule;
import com.foi_bois.zisprojekt.auth.ui.LoginActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = LoginActivityModule.class) //sve aktivnosti, fragmenti i to
    abstract LoginActivity loginActivity();

}