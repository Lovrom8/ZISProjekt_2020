package com.foi_bois.zisprojekt.main.map;

import com.foi_bois.zisprojekt.base.CommonPresenter;
import com.foi_bois.zisprojekt.firebase.BazaHelper;
import com.foi_bois.zisprojekt.lib.Helper;
import com.foi_bois.zisprojekt.main.map.ui.LokacijeView;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;

import javax.inject.Inject;

public class LokacijePresenterImpl<V extends LokacijeView> extends CommonPresenter<V> implements LokacijePresenter<V>, OnMapReadyCallback {

    private GoogleMap map;
    private Marker mojaPozicija, testMark1, testMark2;
    private LocationRequest locationRequest;
    private BazaHelper baza;

    private long INTERVAL = 10 * 1000;
    private long NAJBRZI_INTERVAL = 10 * 1000;

    @Inject
    LokacijePresenterImpl(){}

    @Override
    public void dodajMojuLokaciju() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

}
