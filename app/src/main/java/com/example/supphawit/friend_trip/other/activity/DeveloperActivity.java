package com.example.supphawit.friend_trip.other.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.supphawit.friend_trip.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.supphawit.friend_trip.invitation.activity.RequestListActivity;
import com.example.supphawit.friend_trip.trip.activity.CreateTripActivity;
import com.example.supphawit.friend_trip.trip.activity.TripListActivity;
import com.example.supphawit.friend_trip.user.activity.SignInActivity;
import com.example.supphawit.friend_trip.user.activity.ViewProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeveloperActivity extends AppCompatActivity {

    private static final String TAG = "DeveloperActivity";

    //@BindView(R.id.welcomeuser) TextView welcomeuser;
    @BindView(R.id.devpagetoolbar)
    Toolbar devtoolbar;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bot_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        ButterKnife.bind(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setSupportActionBar(devtoolbar);
        bot_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_viewprofile:
                        viewProfile();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @OnClick(R.id.createtrip_btn)
    public void createTrip() {
        Intent intent = new Intent(this, CreateTripActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.listbtn)
    public void tripList() {
        Intent intent = new Intent(this, TripListActivity.class);
        startActivity(intent);
    }

    public void viewProfile() {
        Intent intent = new Intent(this, ViewProfileActivity.class);
        startActivity(intent);
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    @OnClick(R.id.greenone)
    public void testrequestList(){
        startActivity(new Intent(this, RequestListActivity.class));
        //st@test.com
        //123456789

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "toolbar option selected");
        switch (id) {
            case R.id.log_out_main:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
