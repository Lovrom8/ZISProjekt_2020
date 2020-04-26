package com.foi_bois.zisprojekt.auth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.foi_bois.zisprojekt.R;
import com.foi_bois.zisprojekt.auth.SignupPresenter;
import com.foi_bois.zisprojekt.main.MainActivity;
import com.google.firebase.auth.FirebaseUser;
import com.sdsmdg.tastytoast.TastyToast;


import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class SignupActivity extends DaggerAppCompatActivity implements SignupView {
    @Inject
    SignupPresenter<SignupView> presenter;
    @Inject
    Lazy<FirebaseUser> user;

    private Button btnRegister;
    private Button btnConvertAnon;
    private EditText tbUsername;
    private EditText tbPass;
    private EditText tbConfirmPass;
    private EditText tbEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnRegister = (Button)findViewById(R.id.btnSignup);
        btnRegister.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onSignupClick(v);
            }
        });

        btnConvertAnon = (Button)findViewById(R.id.btnConvertAnon);
        btnConvertAnon.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onConvertAnonClick(v);
            }
        });

        tbUsername = (EditText)findViewById(R.id.editSignupEmail);
        tbPass = (EditText)findViewById(R.id.editSignupPassword);
        tbConfirmPass = (EditText)findViewById(R.id.editConfirmPassword);
        tbEmail = (EditText)findViewById(R.id.editSignupEmail);

        presenter.attach(this);
    }

    @Override
    protected void onDestroy(){
        presenter.detach();
        super.onDestroy();
    }

    public void onSignupClick(View v) {
        presenter.signUpWithEmail(tbUsername.getText().toString(), tbEmail.getText().toString(), tbPass.getText().toString(), tbConfirmPass.getText().toString());
    }

    public void onConvertAnonClick(View v) {
        presenter.convertAnonToNormalUser(tbUsername.getText().toString(), tbEmail.getText().toString(), tbPass.getText().toString(), tbConfirmPass.getText().toString());
    }

    @Override
    public void onPasswordTooShort() {

    }

    @Override
    public void onSignupResult(int signupResult, FirebaseUser user) {
        String msg;
        if(signupResult == 0)
            msg = getResources().getString(R.string.signup_success) + user.getEmail();
        else if(signupResult == 1)
            msg = getResources().getString(R.string.signup_failed);
        else if(signupResult == 2)
            msg = getResources().getString(R.string.signup_tooshort);
        else if(signupResult == 3)
            msg = getResources().getString(R.string.signup_no_pass);
        else if(signupResult == 4)
            msg = getResources().getString(R.string.signup_no_match);
        else if(signupResult == 5)
            msg = getResources().getString(R.string.signup_no_email);
        else
            msg = getResources().getString(R.string.signup_invalid_email);

        if(signupResult == 0) {
            TastyToast.makeText(this, msg, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            startActivity(new Intent(this, MainActivity.class));
        }
        else
            TastyToast.makeText(this, msg, TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
    }

    @Override
    public void onConvertUser(int conversionResult, FirebaseUser user) {
        //TODO: slozi da se ne ponavlja tu i u signupu

    }
}
