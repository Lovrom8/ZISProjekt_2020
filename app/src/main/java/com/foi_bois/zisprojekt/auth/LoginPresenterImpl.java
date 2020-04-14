package com.foi_bois.zisprojekt.auth;

import com.foi_bois.zisprojekt.auth.repo.UserRepository;
import com.foi_bois.zisprojekt.auth.ui.LoginView;
import com.foi_bois.zisprojekt.model.User;

public class LoginPresenterImpl implements LoginPresenter {
    private LoginView view;
    private UserRepository userRepo;

    public LoginPresenterImpl(LoginView view, UserRepository userRepo){
        this.view = view;
        this.userRepo = userRepo;
    }

    public void validateLogin() { //namjerno void !
        try
        {
            User currUser = userRepo.getCurrentUser(); //zove getCurrenUser implementiran u DBUserRepository

            if(currUser == null)
                view.displayWrongPasswordError(currUser);
            else
                view.greetUser(currUser); //greetUser pravi je definiran u LoginActivity!
        } catch(Exception e){
            view.displayError();
        }
    }
}
