package com.foi_bois.zisprojekt.di.auth;

import com.foi_bois.zisprojekt.auth.SignupPresenter;
import com.foi_bois.zisprojekt.auth.SignupPresenterImpl;
import com.foi_bois.zisprojekt.auth.ui.SignupView;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SignupActivityModule {
    @Binds
    abstract SignupPresenter<SignupView> providePresenter(SignupPresenterImpl<SignupView> presenter);
}
