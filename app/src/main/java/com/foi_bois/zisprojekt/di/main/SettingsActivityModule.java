package com.foi_bois.zisprojekt.di.main;

import com.foi_bois.zisprojekt.main.settings.SettingsPresenter;
import com.foi_bois.zisprojekt.main.settings.SettingsPresenterImpl;
import com.foi_bois.zisprojekt.main.settings.ui.SettingsView;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SettingsActivityModule {
    @Binds
    abstract SettingsPresenter<SettingsView> providePresenter(SettingsPresenterImpl<SettingsView> presenter);
}
