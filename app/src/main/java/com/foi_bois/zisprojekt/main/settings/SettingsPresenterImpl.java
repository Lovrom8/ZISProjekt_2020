package com.foi_bois.zisprojekt.main.settings;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.foi_bois.zisprojekt.base.CommonPresenter;
import com.foi_bois.zisprojekt.firebase.BazaHelper;
import com.foi_bois.zisprojekt.main.settings.ui.SettingsView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import javax.inject.Inject;

public class SettingsPresenterImpl<V extends SettingsView> extends CommonPresenter<V> implements SettingsPresenter<V> {

    @Inject
    SettingsPresenterImpl(){}


    @Override
    public void uploadAvatarToFirestore(final Uri path) {
        BazaHelper baza = BazaHelper.getInstance();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null)
            return;

        baza.uploadAvatarToFirestore(currentUser, path, new BazaHelper.FirebaseUploadCallback() {
            @Override
            public void onCallback(boolean isSuccessful, String avatarUrl) {
                if (isSuccessful){
                    BazaHelper.getInstance().setAvatarForUser(currentUser, avatarUrl, new BazaHelper.FirebaseUserCallback() {
                        @Override
                        public void onCallback(boolean isSuccessful) {
                            getView().onAvatarUpload(isSuccessful);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void saveCurrentUserSettings(final String email, final String username) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(username).build();

                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                              if(!task.isSuccessful())
                                    return;

                              BazaHelper.getInstance().setUsernameForUser(user, username, new BazaHelper.FirebaseUserCallback(){
                                  @Override
                                  public void onCallback(boolean isSuccesful) {
                                        getView().onSettingsChanged(isSuccesful);
                                  }
                              });
                        }
                    });
            }
        });
    }
}
