package com.example.supphawit.friend_trip.test;

import com.example.supphawit.friend_trip.BuildConfig;
import com.example.supphawit.friend_trip.activity.SignInActivity;
import com.example.supphawit.friend_trip.presenter.LoginPresenter;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by admin on 5/11/2559.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)

public class SignInActivityTest {

    private SignInActivity signInActivity;

    @Mock
    LoginPresenter presenter;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
    }
}
