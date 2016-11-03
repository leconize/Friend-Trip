package com.example.supphawit.friend_trip.presenter;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;

import com.example.supphawit.friend_trip.model.LoginInteractor;
import com.example.supphawit.friend_trip.view.LoginView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

/**
 * Created by Supphawit on 11/3/2016.
 */

public class LoginPresenter{

    LoginInteractor loginInteractor;
    LoginView loginView;

    private static String TAG = "LoginPresenter";

    public LoginPresenter(LoginInteractor loginInteractor) {
        this.loginInteractor = loginInteractor;
    }

    public void bind(LoginView loginView){
        this.loginView = loginView;
    }

    public void unbind(){
        this.loginView = null;
    }

    public void login(String email, String password){
        if(loginInteractor.validateInput(email) && loginInteractor.validateInput(password)){
            loginInteractor.login(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if(loginView != null){
                        loginView.signinSuccess();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, e.getMessage());
                    if(loginView != null){
                        loginView.sigininFail();
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
