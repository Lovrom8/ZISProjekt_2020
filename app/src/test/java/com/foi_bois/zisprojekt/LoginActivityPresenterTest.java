package com.foi_bois.zisprojekt;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.foi_bois.zisprojekt.presenter.LoginActivityPresenter;
import com.foi_bois.zisprojekt.repositories.UserRepository;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginActivityPresenterTest {

    @Mock
    UserRepository userRepository;

    @Mock
    LoginActivityView view;

    @Test
    public void shouldPassUserToView(){
        //given
        //LoginActivityView view = new MockView();
        /*UserRepository userRepo = new UserRepository() {
            @Override
            public User getCurrentUser() {
                return new User("123", "234");
            }
        }; */

        User currUser = new User("123", "234");
        Mockito.when(userRepository.getCurrentUser()).thenReturn(currUser);

        //when
        LoginActivityPresenter presenter = new LoginActivityPresenter(view, userRepository);
        presenter.getCurrentUser();

        //then
        //Assert.assertEquals(true, ((MockView) view).displayCurrentUser);
        Mockito.verify(view).greetUser(currUser);
    }

    @Test
    public void shouldHandleWrongPassword(){
       // LoginActivityView view = new MockView();
     /*   UserRepository userRepo = new UserRepository() {
            @Override
            public User getCurrentUser() {
                return null;
            }
        }; */

        Mockito.when(userRepository.getCurrentUser()).thenReturn(null);
        LoginActivityPresenter presenter = new LoginActivityPresenter(view, userRepository);
        presenter.getCurrentUser();

        //Assert.assertEquals(true, ((MockView) view).displayNoUser);
        Mockito.verify(view).displayWrongPasswordError(null);
    }

    //GONE :0
    /*
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
    }*/

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