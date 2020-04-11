package com.foi_bois.zisprojekt.presenter;

import com.foi_bois.zisprojekt.LoginActivityView;
import com.foi_bois.zisprojekt.User;
import com.foi_bois.zisprojekt.repositories.UserRepository;

public class LoginActivityPresenter {
    private LoginActivityView view;
    private UserRepository userRepo;

    public LoginActivityPresenter(LoginActivityView view, UserRepository userRepo){
        this.view = view;
        this.userRepo = userRepo;
    }

    public void getCurrentUser() { //namjerno void !
        User currUser = userRepo.getCurrentUser(); //zove getCurrenUser implementiran u DBUserRepository

        if(currUser == null)
            view.displayWrongPasswordError(currUser);
        else
            view.greetUser(currUser); //greetUser pravi je definiran u LoginActivity!
    }
}
