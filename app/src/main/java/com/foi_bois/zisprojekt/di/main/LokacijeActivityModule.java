package com.foi_bois.zisprojekt.di.main;

import com.foi_bois.zisprojekt.main.map.LokacijePresenter;
import com.foi_bois.zisprojekt.main.map.LokacijePresenterImpl;
import com.foi_bois.zisprojekt.main.map.ui.LokacijeView;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class LokacijeActivityModule {
    @Binds
    abstract LokacijePresenter<LokacijeView> providePresenter(LokacijePresenterImpl<LokacijeView> presenter);
}
