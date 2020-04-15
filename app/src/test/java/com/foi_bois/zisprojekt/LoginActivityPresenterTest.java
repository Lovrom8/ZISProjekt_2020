package com.foi_bois.zisprojekt;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import com.foi_bois.zisprojekt.auth.LoginPresenter;
import com.foi_bois.zisprojekt.auth.LoginPresenterImpl;
import com.foi_bois.zisprojekt.auth.ui.LoginView;
import com.foi_bois.zisprojekt.model.User;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginActivityPresenterTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule(); //ako nam bude trebao neki drugi runner, a la Google SDK-a

    @Mock
    LoginView view;
    private LoginPresenter presenter;

    @Before
    public void setUp() {
        presenter = new LoginPresenterImpl(view, userRepository);
    }

    @Test public void shouldPassUserToView() {
        //given
        User currUser = new User("123", "234");
        when(userRepository.getCurrentUser()).thenReturn(currUser);

        //when
        presenter.validateUser();

        //then
        verify(view).greetUser(currUser);
    }

    @Test public void shouldHandleWrongPassword() {
        when(userRepository.getCurrentUser()).thenReturn(null);

        presenter.validateUser();

        verify(view).displayWrongPasswordError(null);
    }

    @Test public void shouldHandleError(){ //ako se pojavi iznimka, test provjerava zove li se u tom slucaju displayError()
        when(userRepository.getCurrentUser()).thenThrow(new RuntimeException("explosion"));

        presenter.validateUser();

        verify(view).displayError();
    }
}