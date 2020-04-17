package com.foi_bois.zisprojekt.auth.ui;

import com.foi_bois.zisprojekt.base.BaseView;
import com.google.firebase.auth.FirebaseUser;

public interface SignupView extends BaseView {
    void onPasswordTooShort();
    void onSignupResult(int signupResult, FirebaseUser user);
    //void
}
