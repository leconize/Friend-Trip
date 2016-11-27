package com.example.supphawit.friend_trip.test;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;

import com.example.supphawit.friend_trip.BuildConfig;
import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.activity.DeveloperActivity;
import com.example.supphawit.friend_trip.activity.SignInActivity;
import com.example.supphawit.friend_trip.model.LoginInteractor;
import com.example.supphawit.friend_trip.presenter.LoginPresenter;
import com.example.supphawit.friend_trip.view.LoginView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.internal.Shadow;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.shadows.ShadowToast;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by admin on 5/11/2559.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)

public class SignInActivityTest {

    private SignInActivity loginView;
    private CountDownLatch clock;
    @Mock
    LoginPresenter presenter;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        FirebaseApp.initializeApp(RuntimeEnvironment.application);
        loginView = Robolectric.setupActivity(SignInActivity.class);
        LoginPresenter shadowloginPresenter = new ShadowloginPresenter();
        shadowloginPresenter.setLoginInteractor(new LoginInteractor());
        shadowloginPresenter.bind(loginView);
        loginView.setLoginPresenter(shadowloginPresenter);

    }

    @Test
    public void testInitValue(){
        Assert.assertEquals(loginView.idinput.getText().toString(), "");
        Assert.assertEquals(loginView.pwdinput.getText().toString(), "");
    }

    @Test
    public void testAuth(){
        Assert.assertNotNull(FirebaseAuth.getInstance());
    }
    @Test
    public void testLoginSuccess(){
        loginView.signinSuccess();
        ShadowActivity shadowActivity = shadowOf(loginView);
        Intent startIntent = shadowActivity.getNextStartedActivity();
        assertNotNull(startIntent);
        assertThat(startIntent.getComponent().getClassName()).isEqualTo(DeveloperActivity.class.getName());
    }

    @Test
    public void testLoginFailAction(){
        loginView.sigininFail();
        Assert.assertEquals("Auth Fail", ShadowToast.getTextOfLatestToast());
    }

    //need to use mock to FirebaseAuth
    public void testLoginFail() throws Exception{
        EditText emailinput = (EditText) loginView.findViewById(R.id.idinput);
        emailinput.setText("fail@fail.com");
        EditText pwdinput = (EditText) loginView.findViewById(R.id.pwdinput);
        pwdinput.setText("test");
        clock = new CountDownLatch(1);
        loginView.loginBtnClick();
        clock.await(30, TimeUnit.SECONDS);
        Assert.assertEquals("Auth Fail", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void testnullinput(){
        loginView.loginBtnClick();
        Assert.assertEquals("Please Fill all Text Field", ShadowToast.getTextOfLatestToast());
    }

    private class ShadowloginPresenter extends LoginPresenter{
        public ShadowloginPresenter() {
            super();
        }

        @Override
        public void login(String email, String password) {
            if(loginInteractor.validateInput(email) && loginInteractor.validateInput(password)){
                loginInteractor.login(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if(loginView != null){
                            loginView.signinSuccess();
                            clock.countDown();
                            System.out.println("pass auth");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(loginView != null){
                            loginView.sigininFail();
                            clock.countDown();
                            System.out.println("fail auth");
                        }
                        else{
                            //warn empty view
                            System.out.println("empty view");
                        }
                    }
                });
            }
            else{
                if(loginView != null){
                    loginView.warnEmptyInput();
                }
            }
        }
    }

}
