package com.foi_bois.zisprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.foi_bois.zisprojekt.presenter.LoginActivityPresenter;
import com.foi_bois.zisprojekt.repositories.impl.DBUserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends FragmentActivity implements LoginActivityView {
    private FirebaseAuth mAuth;
    private Button btnLogin;
    private LoginActivityPresenter presenter;
    private final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginActivityPresenter(this, new DBUserRepository(getApplication()));

        mAuth = FirebaseAuth.getInstance();
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onLoginClick(v);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser currentUser =  mAuth.getCurrentUser();
        if(currentUser != null){
            //TODO
        }
    }

    public void onLoginClick(View v){
        //TODO: actual login
        presenter.getCurrentUser(); //ne bas korisno ATM
        startActivity(new Intent(v.getContext(), MainActivity.class));
    }


    @Override
    public void greetUser(User currentUser) {
        //TODO: promjeni zaslon
        Log.d(TAG, "greeting user: " + currentUser.id);
    }

    @Override
    public void displayWrongPasswordError(User currentUser) {
        //TODO: pokazi nesto korisno
        Log.d(TAG, "kriva sifra brt: ");
    }
}
