package com.example.supphawit.friend_trip.trip.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.trip.model.Trip;
import com.example.supphawit.friend_trip.user.activity.SignInActivity;
import com.example.supphawit.friend_trip.utils.DatabaseUtils;
import com.example.supphawit.friend_trip.utils.UserUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TripDetailActivity extends AppCompatActivity {

    private static final String TAG = TripDetailActivity.class.getName();


    @BindView(R.id.detail_owner) TextView detail_owner;
    @BindView(R.id.detail_max_person) TextView detail_max_person;
    @BindView(R.id.detail_place) TextView detail_place;
    @BindView(R.id.detail_start_date) TextView detail_start_date;
    @BindView(R.id.detail_start_time) TextView detail_start_time;
    @BindView(R.id.detail_end_date) TextView detail_end_date;
    @BindView(R.id.detail_end_time) TextView detail_end_time;
    @BindView(R.id.devpagetoolbar)
    Toolbar devtoolbar;
    Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        ButterKnife.bind(this);
        trip = (Trip) getIntent().getSerializableExtra("trip");
        setValuetoTextViews(trip);
        setSupportActionBar(devtoolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    public void logout() {
        finish();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, SignInActivity.class));
    }

    private void setValuetoTextViews(Trip trip){
        detail_owner.setText(trip.getCreatername());
        detail_start_date.setText(trip.getStartdate());
        detail_start_time.setText(trip.getStarttime());
        detail_end_date.setText(trip.getEnddate());
        detail_end_time.setText(trip.getEndtime());
    }

    @OnClick(R.id.join_btn)
    public void joinBtnClick(){
        DatabaseReference tripMemberRef = DatabaseUtils.getTripRef(trip.getId()).child("joinerId_list");
        final ArrayList<String> memberIdList = trip.getJoinerId_list();
        memberIdList.add(UserUtils.getUserId());
        tripMemberRef.setValue(memberIdList);
    }

    @OnClick(R.id.new_back_btn)
    public void exitBtnClick(){
        finish();
        Intent intent = new Intent(this, TripListActivity.class);
        startActivity(intent);
    }

}
