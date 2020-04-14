package com.foi_bois.zisprojekt.auth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.foi_bois.zisprojekt.menu.MainActivity;
import com.foi_bois.zisprojekt.R;
import com.foi_bois.zisprojekt.auth.LoginPresenter;
import com.foi_bois.zisprojekt.auth.LoginPresenterImpl;
import com.foi_bois.zisprojekt.model.User;
import com.foi_bois.zisprojekt.auth.repo.DBUserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends FragmentActivity implements LoginView {
    private FirebaseAuth mAuth;
    private Button btnLogin;
    private LoginPresenter presenter;
    private final String TAG = LoginActivity.class.getSimpleName();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenterImpl(this, new DBUserRepository(getApplication()));

        mAuth = FirebaseAuth.getInstance();
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onLoginClick(v);
            }
        });
    }

    @Override public void onStart(){
        super.onStart();

        FirebaseUser currentUser =  mAuth.getCurrentUser();
        if(currentUser != null){
            //TODO
        }
    }

    public void onLoginClick(View v){
        //TODO: actual login
        presenter.validateLogin(); //ne bas korisno ATM
        startActivity(new Intent(v.getContext(), MainActivity.class));
    }


    @Override public void greetUser(User currentUser) {
        //TODO: promjeni zaslon
        Log.d(TAG, "greeting user: " + currentUser.getId());
    }

    @Override public void displayWrongPasswordError(User currentUser) {
        //TODO: pokazi nesto korisno
        Log.d(TAG, "kriva sifra brt: ");
    }

    @Override public void displayError(){
        Toast.makeText(this, "Neuspjesno dohvacanje podataka...", Toast.LENGTH_SHORT).show();
    }
}