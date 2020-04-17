package com.foi_bois.zisprojekt.di;

import com.foi_bois.zisprojekt.di.main.LokacijeActivityModule;
import com.foi_bois.zisprojekt.main.map.ui.LokacijeFragment;
import com.foi_bois.zisprojekt.di.main.SettingsActivityModule;
import com.foi_bois.zisprojekt.main.settings.ui.SettingsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class FragmentBindingModule {
    @ContributesAndroidInjector(modules = LokacijeActivityModule.class)
    abstract LokacijeFragment lokacijeFragment();

   @ContributesAndroidInjector(modules = SettingsActivityModule.class)
    abstract SettingsFragment settingsFragment();
}
