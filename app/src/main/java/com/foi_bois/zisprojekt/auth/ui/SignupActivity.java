package com.foi_bois.zisprojekt.auth.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foi_bois.zisprojekt.R;
import com.foi_bois.zisprojekt.auth.LoginPresenter;
import com.foi_bois.zisprojekt.auth.SignupPresenter;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class SignupActivity extends DaggerAppCompatActivity implements SignupView {
    @Inject
    SignupPresenter<SignupView> presenter;
    @Inject
    Lazy<FirebaseUser> user;

    private Button btnRegister;
    private EditText tbUsername;
    private EditText tbPass;
    private EditText tbConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnRegister = (Button)findViewById(R.id.btnSignup);
        btnRegister.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onLoginClick(v);
            }
        });

        tbUsername = (EditText)findViewById(R.id.editSignupEmail);
        tbPass = (EditText)findViewById(R.id.editSignupPassword);
        tbConfirmPass = (EditText)findViewById(R.id.editConfirmPassword);

        presenter.attach(this);
    }

    @Override
    protected void onDestroy(){
        presenter.detach();
        super.onDestroy();
    }

    public void onLoginClick(View v) {
        //TODO: implement
        presenter.signUpWithEmail(tbUsername.getText().toString(), tbPass.getText().toString(), tbConfirmPass.getText().toString());
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onPasswordTooShort() {

    }

    @Override
    public void onSignupResult(int signupResult, FirebaseUser user) {
        //TODO: promjeni zaslon i stavi ove stringove u resurse

        String msg;
        if(signupResult == 0)
            msg = "Uspjeno registrirano " + user.getEmail();
        else if(signupResult == 1)
            msg = "Neuspjesna registracija";
        else if(signupResult == 2)
            msg = "Lozinka previše kratka";
        else if(signupResult == 3)
            msg = "Lozinka nije upisana";
        else
            msg = "Lozinke se ne podudaraju";

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
