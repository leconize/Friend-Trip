package com.example.supphawit.friend_trip.model;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.supphawit.friend_trip.activity.DeveloperActivity;
import com.example.supphawit.friend_trip.activity.SignInActivity;
import com.example.supphawit.friend_trip.utils.UserUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
