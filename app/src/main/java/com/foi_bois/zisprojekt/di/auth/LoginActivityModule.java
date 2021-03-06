package com.foi_bois.zisprojekt.di.auth;

import com.foi_bois.zisprojekt.auth.LoginPresenter;
import com.foi_bois.zisprojekt.auth.LoginPresenterImpl;
import com.foi_bois.zisprojekt.auth.ui.LoginView;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class LoginActivityModule {
    @Binds
    abstract LoginPresenter<LoginView> providePresenter(LoginPresenterImpl<LoginView> presenter);
}