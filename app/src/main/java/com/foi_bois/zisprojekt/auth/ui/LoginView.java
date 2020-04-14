package com.foi_bois.zisprojekt.auth.ui;

import com.foi_bois.zisprojekt.model.User;

public interface LoginView {
    void greetUser(User currentUser);
    void displayWrongPasswordError(User currentUser);
    void displayError();
}
