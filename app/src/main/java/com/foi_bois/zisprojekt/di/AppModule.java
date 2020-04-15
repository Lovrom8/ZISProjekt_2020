package com.foi_bois.zisprojekt.di;

import android.content.Context;

import androidx.annotation.Nullable;

import com.foi_bois.zisprojekt.App;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {
    @Provides
    Context provideContext(App application) {
        return application.getApplicationContext();
    }

    @Nullable
    @Provides
    FirebaseUser provideUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
