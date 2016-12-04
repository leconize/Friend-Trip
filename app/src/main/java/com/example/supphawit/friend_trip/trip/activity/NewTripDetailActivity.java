package com.example.supphawit.friend_trip.trip.activity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.trip.model.Trip;
import com.example.supphawit.friend_trip.trip.activity.TripListActivity;
import com.example.supphawit.friend_trip.user.activity.SignInActivity;
import com.example.supphawit.friend_trip.user.activity.ViewProfileActivity;
import com.example.supphawit.friend_trip.utils.DatabaseUtils;
import com.example.supphawit.friend_trip.utils.StorageUtils;
import com.example.supphawit.friend_trip.utils.UserUtils;
import com.google.common.base.Joiner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewTripDetailActivity extends AppCompatActivity {

    @BindView(R.id.image) ImageView img;
    private static String TAG = "TripDetailActivity";
    private Trip trip;
    @BindView(R.id.trip_detail_ownername) TextView ownername;
    @BindView(R.id.trip_detail_peoplenum) TextView peoplenum;
    @BindView(R.id.trip_detail_starttime) TextView starttime;
    @BindView(R.id.trip_detail_endtime) TextView endtime;
    @BindView(R.id.devpagetoolbar) Toolbar devtoolbar;
    @BindView(R.id.new_join_btn) Button join_btn;
    @BindView(R.id.new_back_btn) Button back_btn;
    @BindView(R.id.detail_profilepic) CircleImageView profile_pic;

    private ArrayList<String> memberIdList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_trip_detail);
        ButterKnife.bind(this);
        trip = (Trip) getIntent().getSerializableExtra("trip");
        Glide.with(this)
                .load("").fitCenter()
                .placeholder(R.drawable.pic)
                .into(img);
        setupText(trip);

    }

    private void setupText(Trip trip){
        memberIdList = trip.getJoinerId_list();
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(trip.getName());
        ownername.setText(trip.getCreatername());
        int currentjoiner = memberIdList.size();
        peoplenum.setText(currentjoiner + "/" + trip.getMaxpeople());
        starttime.setText(trip.getStartdate() + " " + trip.getStarttime());
        endtime.setText(trip.getEnddate() + " " + trip.getEndtime());
        StorageUtils.loadProfilePicture(this, profile_pic, trip.getCreaterid());
        setUpButton();

    }

    private void setUpButton(){
        if(memberIdList.contains(UserUtils.getUserId())){
            back_btn.setVisibility(View.INVISIBLE);
            join_btn.setText("Leave this Trip");
        }
        else if(memberIdList.size() >= trip.getMaxpeople()){
            back_btn.setVisibility(View.INVISIBLE);
            join_btn.setText("Trip is full");
            join_btn.setEnabled(false);
        }
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
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, SignInActivity.class));
    }

    @OnClick(R.id.new_join_btn)
    public void joinBtnClick(){
        DatabaseReference tripMemberRef = DatabaseUtils.getTripRef(trip.getId()).child("joinerId_list");
        final ArrayList<String> memberIdList = trip.getJoinerId_list();
        if(memberIdList.contains(UserUtils.getUserId())){
            memberIdList.remove(UserUtils.getUserId());
        }
        else{
            memberIdList.add(UserUtils.getUserId());
        }

        tripMemberRef.setValue(memberIdList);
        finish();
        startActivity(getIntent());
    }

    @OnClick(R.id.new_back_btn)
    public void exitBtnClick(){
        finish();
        Intent intent = new Intent(this, TripListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ownerbtn)
    public void seeOwnerDetail(){
        Intent intent = new Intent(this, ViewProfileActivity.class);
        intent.putExtra("user_id", trip.getCreaterid());
        startActivity(intent);
    }

    @OnClick(R.id.joinerbtn)
    public void seeJoinerList(){
        Intent intent = new Intent(this, JoinersListActivity.class);
        intent.putExtra("list", trip.getJoinerId_list());
        startActivity(intent);
    }

    @OnClick(R.id.meetingbtn)
    public void seeMeetingPoint(){
        Intent intent = new Intent(this, MapsActivity.class);
        Log.i(TAG, "lat = " + trip.getLatitude());
        intent.putExtra("lat", trip.getLatitude());
        intent.putExtra("lng", trip.getLongitude());
        startActivity(intent);
    }
}
