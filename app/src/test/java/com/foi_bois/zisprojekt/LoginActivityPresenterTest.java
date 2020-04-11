package com.foi_bois.zisprojekt;

import org.junit.Assert;
import org.junit.Test;

import com.foi_bois.zisprojekt.presenter.LoginActivityPresenter;
import com.foi_bois.zisprojekt.repositories.UserRepository;

import java.util.Arrays;

import static org.junit.Assert.*;

public class LoginActivityPresenterTest {

    @Test
    public void shouldPassUserToView(){
        //given
        LoginActivityView view = new MockView();
        UserRepository userRepo = new UserRepository() {
            @Override
            public User getCurrentUser() {
                return new User("123", "234");
            }
        };

        //when
        LoginActivityPresenter presenter = new LoginActivityPresenter(view, userRepo);
        presenter.getCurrentUser();

        //then
        Assert.assertEquals(true, ((MockView) view).displayCurrentUser);
    }

    @Test
    public void shouldHandleWrongPassword(){
        LoginActivityView view = new MockView();
        UserRepository userRepo = new UserRepository() {
            @Override
            public User getCurrentUser() {
                return null;
            }
        };

        LoginActivityPresenter presenter = new LoginActivityPresenter(view, userRepo);
        presenter.getCurrentUser();

        Assert.assertEquals(true, ((MockView) view).displayNoUser);
    }

    private class MockView implements LoginActivityView {
        boolean displayCurrentUser; //programatically test!
        boolean displayNoUser;

        @Override public void greetUser(User currentUser){
            if(currentUser.id.equals("234"))
                displayCurrentUser = true;
        }

        @Override public void displayWrongPasswordError(User currentUser){
            displayNoUser = true;
        }
    }

   /* private class MockUserRepository implements UserRepository {

        private boolean noUser;

        public MockUserRepository(boolean noUser){
                this.noUser = noUser;
        }

        @Override
        public User getCurrentUser(){
            if(noUser)
                return null;
            else
                return new User("123", "234");
        }
    }
    */
}