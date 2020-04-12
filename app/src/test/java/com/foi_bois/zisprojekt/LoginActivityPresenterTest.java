package com.foi_bois.zisprojekt;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import com.foi_bois.zisprojekt.presenter.LoginActivityPresenter;
import com.foi_bois.zisprojekt.repositories.UserRepository;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginActivityPresenterTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule(); //ako bude nam trebao neki drugi runner, a la Google SDK-a

    @Mock
    UserRepository userRepository;

    @Mock
    LoginActivityView view;
    private LoginActivityPresenter presenter;

    @Before
    public void setUp() {
        presenter = new LoginActivityPresenter(view, userRepository);
    }

    @Test
    public void shouldPassUserToView() {
        //given
        User currUser = new User("123", "234");
        when(userRepository.getCurrentUser()).thenReturn(currUser);

        //when
        presenter.getCurrentUser();

        //then
        verify(view).greetUser(currUser);
    }

    @Test
    public void shouldHandleWrongPassword(){
        when(userRepository.getCurrentUser()).thenReturn(null);

        presenter.getCurrentUser();

        verify(view).displayWrongPasswordError(null);
    }
}