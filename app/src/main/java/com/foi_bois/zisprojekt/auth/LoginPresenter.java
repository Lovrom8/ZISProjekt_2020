package com.foi_bois.zisprojekt.auth;

import com.foi_bois.zisprojekt.auth.ui.LoginView;
import com.foi_bois.zisprojekt.base.BasePresenter;

public interface LoginPresenter<V extends LoginView> extends BasePresenter<V> {
    void logInWithEmailPass(String email, String password);
    void logInWithNumber();
    void logInAnon();
    void checkLogin();
}
