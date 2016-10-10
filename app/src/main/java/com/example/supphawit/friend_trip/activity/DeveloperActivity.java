package com.example.supphawit.friend_trip.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.supphawit.friend_trip.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.supphawit.friend_trip.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class DeveloperActivity extends AppCompatActivity {

    private static final String TAG = "DeveloperActivity";
    private User loginuser;

    @BindView(R.id.welcomeuser) TextView welcomeuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        ButterKnife.bind(this);
        loginuser = getIntent().getParcelableExtra("loginuser");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            welcomeuser.setText("Welcome " + user.getEmail());
        } else {
            welcomeuser.setText("Who are you? You are, somehow, not logged in");
        }
    }

    @OnClick(R.id.createtrip_btn)
    public void createTrip(){
        Intent intent = new Intent(this, CreateTripActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.viewprofilebt)
    public void viewProfile(){
        Intent intent = new Intent(this, ViewProfileActivity.class);
        intent.putExtra("loginuser", loginuser);
        startActivity(intent);
    }
}
