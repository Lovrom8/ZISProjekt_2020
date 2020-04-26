package com.foi_bois.zisprojekt.di;


import com.foi_bois.zisprojekt.App;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules =
        {AndroidSupportInjectionModule.class,
        ActivityBindingModule.class,
        FragmentBindingModule.class,
        AppModule.class})

public interface AppComponent extends AndroidInjector<App> {
    void inject(App application);

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<App> { }


}
