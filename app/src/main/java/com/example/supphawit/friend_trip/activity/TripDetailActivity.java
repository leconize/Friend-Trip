package com.example.supphawit.friend_trip.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.model.Trip;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripDetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_owner) TextView detail_owner;
    @BindView(R.id.detail_max_person) TextView detail_max_person;
    @BindView(R.id.detail_place) TextView detail_place;
    @BindView(R.id.detail_start_date) TextView detail_start_date;
    @BindView(R.id.detail_start_time) TextView detail_start_time;
    @BindView(R.id.detail_end_date) TextView detail_end_date;
    @BindView(R.id.detail_end_time) TextView detail_end_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        ButterKnife.bind(this);
        Trip trip = (Trip) getIntent().getSerializableExtra("trip");
        setValuetoTextViews(trip);
    }

    private void setValuetoTextViews(Trip trip){
        detail_owner.setText(trip.getCreatername());
        detail_place.setText(trip.getPlaceString());
        detail_start_date.setText(trip.getStartdate());
        detail_start_time.setText(trip.getStarttime());
        detail_end_date.setText(trip.getEnddate());
        detail_end_time.setText(trip.getEndtime());
    }
}
