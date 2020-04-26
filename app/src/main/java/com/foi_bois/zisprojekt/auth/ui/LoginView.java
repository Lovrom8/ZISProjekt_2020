package com.foi_bois.zisprojekt.auth.ui;

import com.foi_bois.zisprojekt.base.BaseView;
import com.foi_bois.zisprojekt.model.User;
import com.google.firebase.auth.FirebaseUser;

public interface LoginView extends BaseView {
    void onCheckLogin(boolean isLoggedIn);
    void onLoginResult(boolean isSuccess, FirebaseUser user);
    void onAnonLoginResult(boolean isSuccess, FirebaseUser user);
}
