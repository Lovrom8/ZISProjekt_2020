package com.foi_bois.zisprojekt.firebase;

import android.content.Context;
import android.content.res.Resources;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.foi_bois.zisprojekt.R;
import com.foi_bois.zisprojekt.lib.Constants;
import com.foi_bois.zisprojekt.lib.Helper;
import com.foi_bois.zisprojekt.model.Location;
import com.foi_bois.zisprojekt.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class LokacTip extends HashMap<String, Double> {}

public class BazaHelper  {
    private FirebaseDatabase DB;
    private FirebaseAuth auth;
    private FirebaseStorage storageRef;
    private DatabaseReference dbRef, dbUserRef;
    private static final String TAG = "Baza";
    private HashMap<String, Object> DBObj;
    public Map<String, Location> lokacije;

    private static class SingletonHolder { //bazu bismo načelno trebali instancirati samo jednom, pa je singleton super tu
        private static final BazaHelper INSTANCE = new BazaHelper();
    }

    public static BazaHelper getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public FirebaseAuth getAuth(){
        return FirebaseAuth.getInstance();
    }

    public BazaHelper(){
        lokacije = new HashMap<String, Location>();
        DB = FirebaseDatabase.getInstance();
        auth = getAuth();
        dbRef = DB.getReference("lokacije");
        dbUserRef = DB.getReference("users");
        storageRef = FirebaseStorage.getInstance();
    }

    public interface FirebaseUserCallback{ void onCallback(boolean isSuccesful); }
    public interface FirebaseUserLoadCallback {  void onCallback(boolean isSuccessful, User userData); }

    public void addNewUserCustomData(FirebaseUser user, User customData, final FirebaseUserCallback callback){
        dbUserRef.child(user.getUid()).setValue(customData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onCallback(task.isSuccessful());
            }
        });
    }

    public void getCustomDataForUser(FirebaseUser user, final FirebaseUserLoadCallback callback){
        dbUserRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userData = dataSnapshot.getValue(User.class); //JSON to POJO Poggers
                callback.onCallback(true, userData);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                Log.w(TAG, Resources.getSystem().getString(R.string.neuspjesno_citanje_baze), error.toException());
            }
        });
    }

    public void setAvatarForUser(FirebaseUser user, String path, final FirebaseUserCallback callback){
        dbUserRef.child(user.getUid()).child("avatar").setValue(path)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful());
                    }
                });
    }

    public interface FirebaseUploadCallback{
        void onCallback(boolean isSuccessful, String avatarUrl);
    }

    public void uploadAvatarToFirestore(FirebaseUser user, Uri uri, final FirebaseUploadCallback callback){
        final StorageReference avatarRef = storageRef.getReference("avatars").child(user.getUid());

        avatarRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) { //malo uzasa jer je i getDownloadUrl Task :(
                if(task.isSuccessful()){
                   avatarRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           callback.onCallback(true, uri.toString());
                       }
                   });
                }
                else {
                    callback.onCallback(false, "");
                }
            }
        });
    }

    public void addDefaultFieldsForUser(final FirebaseUser user){
        User customFields = new User(Constants.DEFAULT_AVATAR_URL, new Location(0.0, 0.0), 0); //locationUpdated 0 znaci da bude postavlejno na 1.1.1970 ilitiga sigurno prije "izlaska" aplikacije (:
        dbUserRef.child(user.getUid()).setValue(customFields).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("BAZA", "Uspjesno spremljeno u bazu");
                }else{
                    Log.d("BAZA", "Problem sa spremanjem fieldova u DB za UID: " + user.getUid());
                }
            }
        });
    }

    //TODO: prebaci ostalo

    public interface FirebaseCallback {
        void onCallback(Map<String, Location> lokacije);
    }

    //TODO: ovo ne radi više jer nije više ista Location... :(
    public void procitajPodatke(final FirebaseCallback firebaseCallback){
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //lokacije
                    HashMap<String, LokacTip> cijelo = (HashMap<String, LokacTip>) snapshot.getValue(); //location:lat-lng
                    HashMap<String, Double> lokacija = (HashMap<String, Double>)cijelo.get("location");

                    double lat = lokacija.get("latitude");
                    double lng = lokacija.get("longitude");

                    Location value = new Location(lat, lng);
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


    public void osvjeziLokaciju(Context ctx, Location location, String id){
        Map<String, Object> entry  = new HashMap<>();
        entry.put(id, location);

        dbRef.updateChildren(entry);
    }

    public void osvjeziMojuLokaciju(Context ctx, Location location){
        String uniqueID = Helper.id(ctx);
        osvjeziLokaciju(ctx, location, uniqueID);
    }

    public void izbrisiLokaciju(String id) {
        dbRef.child(id).removeValue();
    }
}
