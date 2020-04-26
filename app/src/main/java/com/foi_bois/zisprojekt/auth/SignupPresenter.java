package com.foi_bois.zisprojekt.auth;

import com.foi_bois.zisprojekt.auth.ui.SignupView;
import com.foi_bois.zisprojekt.base.BasePresenter;
import com.google.firebase.auth.FirebaseUser;

public interface SignupPresenter<V extends SignupView> extends BasePresenter<V> {
    void signUpWithEmail(String username, String email, String password, String confirmPassword);
    void convertAnonToNormalUser(String username, String email, String password, String confirmPassword);
}
