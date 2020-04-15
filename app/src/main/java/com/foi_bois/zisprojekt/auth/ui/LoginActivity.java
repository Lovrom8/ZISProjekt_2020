package com.foi_bois.zisprojekt.auth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.foi_bois.zisprojekt.menu.MainActivity;
import com.foi_bois.zisprojekt.R;
import com.foi_bois.zisprojekt.auth.LoginPresenter;
import com.foi_bois.zisprojekt.auth.LoginPresenterImpl;
import com.foi_bois.zisprojekt.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import javax.inject.Inject;
import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class LoginActivity extends DaggerAppCompatActivity implements LoginView {
    @Inject
    LoginPresenter<LoginView> presenter;
    @Inject
    Lazy<FirebaseUser> user;

    private FirebaseAuth mAuth;
    private Button btnLogin;
    private EditText tbUsername;
    private EditText tbPass;
    private final String TAG = LoginActivity.class.getSimpleName();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onLoginClick(v);
            }
        });

        tbUsername = (EditText)findViewById(R.id.editEmail);
        tbPass = (EditText)findViewById(R.id.editPassword);

        presenter.attach(this);
        presenter.checkLogin();
    }

    @Override protected void onDestroy(){
        presenter.detach();
        super.onDestroy();
    }

    public void onLoginClick(View v){
        String email = tbUsername.getText().toString();
        String pass = tbPass.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Not Valid!", Toast.LENGTH_SHORT).show();
            return;
        }

        presenter.logInWithEmailPass(email, pass);
       // startActivity(new Intent(v.getContext(), MainActivity.class));
    }

    @Override public void onCheckLogin(boolean isLoggedIn){
        if(isLoggedIn)
            Toast.makeText(this, "Vec ste prijavljeni!", Toast.LENGTH_SHORT).show();
    }

    @Override public void onLoginResult(boolean isSuccess, FirebaseUser user) {
        //TODO: promjeni u neku bolji pozdravn
        if(isSuccess)
            Toast.makeText(this, "Dobrodosli " + user.getEmail(), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Pepehands", Toast.LENGTH_SHORT).show();
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

    @Override
    public void showLoading() {
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideLoading() {
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
    }
}