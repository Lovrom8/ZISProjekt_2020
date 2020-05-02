package com.foi_bois.zisprojekt.main.map;

import com.foi_bois.zisprojekt.base.BasePresenter;
import com.foi_bois.zisprojekt.main.map.ui.LokacijeView;

public interface LokacijePresenter<V extends LokacijeView> extends BasePresenter<V> {
    void loadOtherLocations();
    void createTestMarkers(boolean addOthers);
}
