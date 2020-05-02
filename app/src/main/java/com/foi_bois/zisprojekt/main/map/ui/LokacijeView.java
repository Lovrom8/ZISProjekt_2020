package com.foi_bois.zisprojekt.main.map.ui;

import com.foi_bois.zisprojekt.base.BaseView;
import com.google.android.gms.maps.model.LatLng;

public interface LokacijeView extends BaseView {
    void addMyLocation(LatLng myPos);
    void addPositionMarker(LatLng pos, String title);
    void clearMap();
}
