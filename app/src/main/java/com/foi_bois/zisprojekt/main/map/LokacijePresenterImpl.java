package com.foi_bois.zisprojekt.main.map;

import com.foi_bois.zisprojekt.base.CommonPresenter;
import com.foi_bois.zisprojekt.firebase.BazaHelper;
import com.foi_bois.zisprojekt.main.map.ui.LokacijeView;
import com.foi_bois.zisprojekt.model.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

public class LokacijePresenterImpl<V extends LokacijeView> extends CommonPresenter<V> implements LokacijePresenter<V> {
    @Inject
    LokacijePresenterImpl(){}


    @Override
    public void loadOtherLocations() {
        final BazaHelper db = BazaHelper.getInstance();
        final long maxDiff = 60 * 1000;

        db.readAllUserData(new BazaHelper.FirebaseAllUserCallback() {
            @Override
            public void onCallback(HashMap<String, User> userDataList) {
                getView().clearMap();

                String mojId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                long unixTime = System.currentTimeMillis() / 1000;

                for(String id : userDataList.keySet()){
                    User userData = userDataList.get(id);

                    if(id.equals(mojId))
                        getView().addMyLocation(userData.getLocation().getLatLng());
                     else if (unixTime - userData.getLocationUpdated() <= maxDiff )
                         getView().addPositionMarker(userData.getLocation().getLatLng(), userData.getUsername());
                }
            }
        });
    }

    @Override
    public void createTestMarkers(boolean addOthers) {
        LatLng yourPos = new LatLng(45.8150, 15.9819); //Zagreb

        ArrayList<LatLng> testLocations = new ArrayList<LatLng>();
        testLocations.add(new LatLng(48.2082, 16.3738)); //Beč
        testLocations.add(new LatLng(48.8566, 2.3522)); //Paris

        getView().addMyLocation(yourPos);

        if(addOthers) {
            for(LatLng testPos : testLocations){
                getView().addPositionMarker(testPos, "Testna pozicija");
            }
        }
    }
}
