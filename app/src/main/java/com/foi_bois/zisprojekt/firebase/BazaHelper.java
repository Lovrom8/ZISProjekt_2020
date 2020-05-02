package com.foi_bois.zisprojekt.firebase;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.foi_bois.zisprojekt.R;
import com.foi_bois.zisprojekt.lib.Constants;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public interface FirebaseAllUserCallback { void onCallback(HashMap<String, User> users); }
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

    public void setUsernameForUser(FirebaseUser user, String newUsername, final FirebaseUserCallback callback){
        dbUserRef.child(user.getUid()).child("username").setValue(newUsername)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful());
                    }
                });
    }

    public void refreshMyLocation(Location location, final FirebaseUserCallback callback){
        refreshLocationForUser(FirebaseAuth.getInstance().getCurrentUser(), location, callback);
    }

    public void refreshLocationForUser(FirebaseUser user, Location location, final FirebaseUserCallback callback){
        dbUserRef.child(user.getUid()).child("location").setValue(location)
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

    public void addDefaultFieldsForUser(final FirebaseUser user, @Nullable String username){
        User customFields = new User(username, Constants.DEFAULT_AVATAR_URL, new Location(0.0, 0.0), 0); //locationUpdated 0 znaci da bude postavlejno na 1.1.1970 ilitiga sigurno prije "izlaska" aplikacije (:
        dbUserRef.child(user.getUid()).setValue(customFields).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("BAZA",  Resources.getSystem().getString(R.string.log_savedInDBSuccess));
                }else{
                    Log.d("BAZA", Resources.getSystem().getString(R.string.log_savedInDBSuccess) + " for UID: " + user.getUid());
                }
            }
        });
    }

    public void readAllUserData(final FirebaseAllUserCallback firebaseCallback){
        dbUserRef.addValueEventListener(new ValueEventListener() {
            HashMap<String, User> ret = new HashMap<String, User>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshotChild : dataSnapshot.getChildren()) {
                    User userData = snapshotChild.getValue(User.class);
                    ret.put(snapshotChild.getKey(), userData);
                }

                firebaseCallback.onCallback(ret);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, Resources.getSystem().getString(R.string.neuspjesno_citanje_baze), databaseError.toException());

                firebaseCallback.onCallback(ret);
            }
        });
    }

    //TODO: prebaci ostalo

    public interface FirebaseCallback {
        void onCallback(Map<String, Location> lokacije);
    }

    //TODO: glorious, al ne radi više jer nije ista Location... :(
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

    public void removeLocation(String id) {
        dbRef.child(id).removeValue();
    }
}
