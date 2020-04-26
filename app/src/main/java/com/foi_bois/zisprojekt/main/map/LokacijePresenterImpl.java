package com.foi_bois.zisprojekt.main.map;

import com.foi_bois.zisprojekt.base.CommonPresenter;
import com.foi_bois.zisprojekt.main.map.ui.LokacijeView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import javax.inject.Inject;

public class LokacijePresenterImpl<V extends LokacijeView> extends CommonPresenter<V> implements LokacijePresenter<V>, OnMapReadyCallback {

    @Inject
    LokacijePresenterImpl(){}

    @Override
    public void dodajMojuLokaciju() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    private void ucitajDrugePozicije(){

    }

}
