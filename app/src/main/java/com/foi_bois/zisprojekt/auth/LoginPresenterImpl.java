package com.foi_bois.zisprojekt.auth;

import androidx.annotation.NonNull;

import com.foi_bois.zisprojekt.auth.ui.LoginView;
import com.foi_bois.zisprojekt.base.CommonPresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.Lazy;

public class LoginPresenterImpl<V extends LoginView> extends CommonPresenter<V> implements LoginPresenter<V> {
    private FirebaseUser user;

    @Inject
    LoginPresenterImpl(Lazy<FirebaseUser> user){ this.user = user.get(); }

    @Override
    public void logInWithEmailPass(String email, String password) {
        if(email.length() == 0 || password.length() == 0)
            return;

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            getView().onLoginResult(true, task.getResult().getUser());
                        } else {
                            getView().onLoginResult(false, null);
                        }
                    }
                });
    }


    @Override
    public void checkLogin(){ //ako je FB user null, onda nije ulogiran.. wowsies!
        getView().onCheckLogin(user == null);
    }

    @Override
    public void logInAnon(){
        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            getView().onAnonLoginResult(true, task.getResult().getUser());
                        } else {
                            getView().onAnonLoginResult(false, null);
                        }
                    }
                });
    }

/*    public void validateUser(){
        try
        {
            User currUser = userRepo.getCurrentUser(); //zove getCurrenUser iz interactorak koji zove pravu stvar implementiranu u DBUserRepository

            if(currUser == null)
                view.displayWrongPasswordError(currUser);
            else
                view.greetUser(currUser); //greetUser pravi je definiran u LoginActivity!
        } catch(Exception e){
            view.displayError();
        }
    }
*/
}