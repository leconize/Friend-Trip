package com.example.supphawit.friend_trip.trip.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.util.Log;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.invitation.activity.RequestListActivity;
import com.example.supphawit.friend_trip.trip.adapter.TripAdapter;
import com.example.supphawit.friend_trip.trip.model.Trip;
import com.example.supphawit.friend_trip.user.activity.SignInActivity;
import com.example.supphawit.friend_trip.user.activity.ViewProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripListActivity extends AppCompatActivity{

    private static final String TAG = "TripListActivity";
    private List<Trip> trips;
    private TripAdapter tripAdapter;
    @BindView(R.id.rvTrip) RecyclerView recyclerView;
    @BindView(R.id.devpagetoolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);
        ButterKnife.bind(this);
        trips = new ArrayList<>();
        tripAdapter = new TripAdapter(trips, this);
        recyclerView.setAdapter(tripAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        DatabaseReference trip_ref = FirebaseDatabase.getInstance().getReference("trips");
        //searchView.setOnQueryTextListener(this);
        setSupportActionBar(toolbar);
        trip_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.i(TAG, "Add trip id:" + dataSnapshot.getRef().getKey());
                Trip trip = dataSnapshot.getValue(Trip.class);
                trip.setId(dataSnapshot.getKey());
                trips.add(trip);
                tripAdapter.notifyItemInserted(trips.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                //NOT important right now i don't think we need realtime list show
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Trip trip = dataSnapshot.getValue(Trip.class);
                trips.remove(trips);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getCode()+ " " + databaseError.getMessage());
            }
        });

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
            case R.id.create_trip_bar:
                createtrip();
                return true;
            case R.id.view_profile_bar:
                viewprofile();
                return true;
            case R.id.log_out_main:
                logout();
                return true;
            case R.id.mail_noti:
                checkNoti();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void viewprofile(){
        Intent intent = new Intent(this, ViewProfileActivity.class);
        startActivity(intent);
    }

    public void createtrip(){
        Intent intent = new Intent(this, CreateTripActivity.class);
        startActivity(intent);
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    public void checkNoti(){
        Intent intent = new Intent(this, RequestListActivity.class);
        startActivity(intent);
    }

    private static List<Trip> filter(List<Trip> trips, String query){
        final String lowCaseQuery = query.toLowerCase();

//        final List<Trip> filterList = new ArrayList<>();
//        for(Trip trip: trips){
//            final String text = trip.getPlaceString().toLowerCase();
//            if(text.contains(lowCaseQuery)){
//                filterList.add(trip);
//            }
//        }
//        return filterList;
        return null;
    }
}
