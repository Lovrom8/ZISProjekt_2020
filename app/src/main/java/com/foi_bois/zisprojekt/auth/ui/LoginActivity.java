package com.foi_bois.zisprojekt.auth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.util.Pair;

import com.foi_bois.zisprojekt.lib.AuthHelper;
import com.foi_bois.zisprojekt.main.MainActivity;
import com.foi_bois.zisprojekt.R;
import com.foi_bois.zisprojekt.auth.LoginPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdsmdg.tastytoast.TastyToast;

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
    private TextView tvSignup;
    private TextView tvAnonLogin;
    private CheckBox checkShowHidePass;
    private CheckBox checkRememberMe;

    private final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkIfRememberMeEnabled();

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onLoginClick(v);
            }
        });

        tvSignup = (TextView)findViewById(R.id.txtSignup);
        tvSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onSignupClick(v);
            }
        });
        tvAnonLogin = (TextView)findViewById(R.id.txtAnonLogin);
        tvAnonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onAnonLoginClick(v);
            }
        });

        tbUsername = (EditText)findViewById(R.id.editLoginEmail);
        tbPass = (EditText)findViewById(R.id.editLoginPassword);

        checkShowHidePass = (CheckBox)findViewById(R.id.checkShowHidePassword);
        checkShowHidePass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onCheckShowPassClicked(v);
            }
        });

        checkRememberMe = (CheckBox)findViewById(R.id.checkRememberMe);
        checkRememberMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckRememberMeClicked();
            }
        });

        presenter.attach(this);
        //presenter.checkLogin();
    }

    @Override
    protected void onDestroy(){
        presenter.detach();
        super.onDestroy();
    }

    public void onSignupClick(View v){
        startActivity(new Intent(this, SignupActivity.class));
    }

    public void onLoginClick(View v){
        String email = tbUsername.getText().toString();
        String pass = tbPass.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            TastyToast.makeText(this, getResources().getString(R.string.login_missing), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
            return;
        }

        presenter.logInWithEmailPass(email, pass);
    }

    public void onAnonLoginClick(View v) {
        presenter.logInAnon();
    }

    @Override
    public void onCheckLogin(boolean isLoggedIn){
        if(isLoggedIn)
            TastyToast.makeText(this, getResources().getString(R.string.login_already_logged), Toast.LENGTH_SHORT, TastyToast.INFO).show();
    }

    @Override
    public void onLoginResult(boolean isSuccess, FirebaseUser user) {
        if(isSuccess){
            TastyToast.makeText(this, getResources().getString(R.string.login_success) + user.getEmail(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            startActivity(new Intent(this, MainActivity.class));
        }
        else
            TastyToast.makeText(this, getResources().getString(R.string.login_failed), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
    }

    @Override
    public void onAnonLoginResult(boolean isSuccess, FirebaseUser user) {
        if(isSuccess) {
            TastyToast.makeText(this, getResources().getString(R.string.login_anon_success), Toast.LENGTH_SHORT, TastyToast.INFO).show();
            startActivity(new Intent(this, MainActivity.class));
        }
        else
            TastyToast.makeText(this, getResources().getString(R.string.login_anon_failed), Toast.LENGTH_SHORT, TastyToast.ERROR).show();
    }

    private void onCheckShowPassClicked(View v){
        if(checkShowHidePass.isChecked())
            tbPass.setTransformationMethod(new PasswordTransformationMethod());
        else
            tbPass.setTransformationMethod(null);
    }

    private void onCheckRememberMeClicked(){
        if(checkRememberMe.isChecked())
           AuthHelper.saveLoginCredsToPerfs(getApplicationContext(), tbUsername.getText().toString(), tbPass.getText().toString());
        else
            AuthHelper.removeSavedLoginCreds(getApplicationContext());
    }

    private void checkIfRememberMeEnabled(){
        if(AuthHelper.isRememberMeChecked(getApplicationContext())){
            Pair<String, String> creds = AuthHelper.getSavedLoginCreds(getApplicationContext());
            presenter.logInWithEmailPass(creds.first, creds.second);
        }
    }
}