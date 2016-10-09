package com.example.supphawit.friend_trip.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.supphawit.friend_trip.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeveloperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.createtrip_btn)
    public void createTrip(){
        Intent intent = new Intent(this, CreateTripActivity.class);
        startActivity(intent);
    }
}
