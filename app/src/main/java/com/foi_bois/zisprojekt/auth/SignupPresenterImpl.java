package com.foi_bois.zisprojekt.auth;

import androidx.annotation.NonNull;

import com.foi_bois.zisprojekt.auth.ui.SignupView;
import com.foi_bois.zisprojekt.base.CommonPresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.Lazy;

public class SignupPresenterImpl<V extends SignupView> extends CommonPresenter<V> implements SignupPresenter<V> {
    private FirebaseUser user;

    @Inject
    SignupPresenterImpl(Lazy<FirebaseUser> user){
        this.user = user.get();
    }

    @Override
    public void signUpWithEmail(String email, String password, String confirmPassword) {
        if(password.length() == 2){
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

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getView().onSignupResult(0, task.getResult().getUser());
                            // startActivity(new Intent(SignUpActivity.this, EditProfileActivity.class));
                        }
                        else {
                            getView().onSignupResult(1, null);
                        }
                    }
                });
    }

    @Override
    public void signUpWithNumber() {
        //TODO: implement sign up with number
    }
}
