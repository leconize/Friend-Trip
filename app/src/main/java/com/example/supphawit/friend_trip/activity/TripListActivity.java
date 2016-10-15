package com.example.supphawit.friend_trip.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.adapter.TripAdapter;
import com.example.supphawit.friend_trip.model.Trip;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripListActivity extends AppCompatActivity {

    private static final String TAG = TripListActivity.class.getName();
    @BindView(R.id.rvTrip) RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);
        ButterKnife.bind(this);
        final List<Trip> trips = new ArrayList<>();
        final TripAdapter tripAdapter = new TripAdapter(trips, this);
        recyclerView.setAdapter(tripAdapter);
        DatabaseReference test = FirebaseDatabase.getInstance().getReference("trips");
        test.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.i(TAG, dataSnapshot.getRef().toString());
                Trip trip = dataSnapshot.getValue(Trip.class);
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    ((TextView) findViewById(R.id.textView)).setText("IT's work");
                    Log.i(TAG, child.getRef().toString());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

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
}
