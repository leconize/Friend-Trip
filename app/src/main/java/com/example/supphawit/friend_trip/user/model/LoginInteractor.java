package com.example.supphawit.friend_trip.user.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Supphawit on 11/3/2016.
 */

public class LoginInteractor {

    private static String TAG = "LoginInteractor";

    public LoginInteractor(){

    }

    public Task<AuthResult> login(String email, String password){
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password);
    }

    public boolean validateInput(String input){
        return !input.equals("");
    }
}
