package com.foi_bois.zisprojekt.auth;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.foi_bois.zisprojekt.auth.ui.SignupView;
import com.foi_bois.zisprojekt.base.CommonPresenter;
import com.foi_bois.zisprojekt.firebase.BazaHelper;
import com.foi_bois.zisprojekt.lib.AuthHelper;
import com.foi_bois.zisprojekt.lib.Helper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import javax.inject.Inject;

import dagger.Lazy;

public class SignupPresenterImpl<V extends SignupView> extends CommonPresenter<V> implements SignupPresenter<V> {
    private FirebaseUser user;

    @Inject
    SignupPresenterImpl(Lazy<FirebaseUser> user){
        this.user = user.get();
    }

    @Override
    public void signUpWithEmail(final String username, String email, String password, String confirmPassword) {
        if(password.length() <= 5){
            getView().onSignupResult(2, null);
            return;
        }

        if(password.length() == 0){
            getView().onSignupResult(3, null);
            return;
        }

        if(!password.equals(confirmPassword)){
            getView().onSignupResult(4, null);
            return;
        }

        if(email.length() == 0){
            getView().onSignupResult(5, null);
            return;
        }

        if(!AuthHelper.isEmailValid(email)){
            getView().onSignupResult(6, null);
            return;
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    public void onComplete(final @NonNull Task<AuthResult> signupTask) {
                        if (signupTask.isSuccessful()){
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username).build();

                            user = FirebaseAuth.getInstance().getCurrentUser();
                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if(task.isSuccessful()) {
                                         getView().onSignupResult(0, signupTask.getResult().getUser());
                                         BazaHelper.getInstance().addDefaultFieldsForUser(user); //TODO: prebaci da bolje pase, al bi moglo ispast tri sloja onComplete-ova :(
                                     }
                                     else {
                                         getView().onSignupResult(1, null);
                                     }
                                 }
                             });
                        }
                     //   else
                     //       getView().onSignupResult(1, null);
                    }
                });
    }

    @Override
    public void convertAnonToNormalUser(String username, String email, String password, String confirmPassword) {
        //TODO: slozi na UI-ju

        if(email.length() == 0 || password.length() == 0){
            return;
        }

        AuthCredential creds = EmailAuthProvider.getCredential(email, password);
        FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(creds)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                        getView().onSignupResult(0, task.getResult().getUser());
                    else
                        getView().onSignupResult(1, null);
                }
            });
    }

}
