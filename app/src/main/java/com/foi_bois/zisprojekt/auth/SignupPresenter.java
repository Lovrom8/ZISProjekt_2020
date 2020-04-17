package com.foi_bois.zisprojekt.auth;

import com.foi_bois.zisprojekt.auth.ui.SignupView;
import com.foi_bois.zisprojekt.base.BasePresenter;

public interface SignupPresenter<V extends SignupView> extends BasePresenter<V> {
    void signUpWithEmail(String email, String password, String confirmPassword);
    void signUpWithNumber();
}
