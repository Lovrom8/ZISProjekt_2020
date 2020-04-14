package com.foi_bois.zisprojekt.firebase;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;

import com.foi_bois.zisprojekt.R;
import com.foi_bois.zisprojekt.lib.Helper;
import com.foi_bois.zisprojekt.model.Lokacija;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

class LokacTip extends HashMap<String, Double> {}

public class Baza  {
    private FirebaseDatabase DB;
    private DatabaseReference dbRef;
    private static final String TAG = "Baza";
    private HashMap<String, Object> DBObj;
    public Map<String, Lokacija> lokacije;

    public Baza(){
        lokacije = new HashMap<String, Lokacija>();
        DB = FirebaseDatabase.getInstance();
        dbRef = DB.getReference("lokacije"); //vazno jer je citanje asinkrono!
    }

    public interface FirebaseCallback {
        void onCallback(Map<String, Lokacija> lokacije);
    }

    public void procitajPodatke(final FirebaseCallback firebaseCallback){
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //lokacije
                    HashMap<String, LokacTip> cijelo = (HashMap<String, LokacTip>) snapshot.getValue(); //location:lat-lng
                    HashMap<String, Double> lokacija = (HashMap<String, Double>)cijelo.get("location");

                    double lat = lokacija.get("latitude");
                    double lng = lokacija.get("longitude");

                    Lokacija value = new Lokacija(lat, lng);
                    lokacije.put(snapshot.getKey(), value); //key = id
                }

                firebaseCallback.onCallback(lokacije);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                Log.w(TAG, Resources.getSystem().getString(R.string.neuspjesno_citanje_baze), error.toException());
            }
        });
    }


    public void osvjeziLokaciju(Context ctx, Lokacija lokacija, String id){
        Map<String, Object> entry  = new HashMap<>();
        entry.put(id, lokacija);

        dbRef.updateChildren(entry);
    }

    public void osvjeziMojuLokaciju(Context ctx, Lokacija lokacija){
        String uniqueID = Helper.id(ctx);
        osvjeziLokaciju(ctx, lokacija, uniqueID);
    }


    public void izbrisiLokaciju(String id) {
        dbRef.child(id).removeValue();
    }
}
