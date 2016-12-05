package com.example.supphawit.friend_trip.invitation.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.invitation.adapter.InviUserAdapter;
import com.example.supphawit.friend_trip.trip.model.Trip;
import com.example.supphawit.friend_trip.user.model.User;
import com.example.supphawit.friend_trip.utils.DatabaseUtils;
import com.example.supphawit.friend_trip.utils.UserUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseInviActivity extends AppCompatActivity {


    @BindView(R.id.invi_userlist)
    RecyclerView recyclerView;
    ArrayList<User> users;
    Trip trip;

    private static final String TAG = "ChooseInviActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_invi);
        ButterKnife.bind(this);
        users = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        trip = (Trip) getIntent().getSerializableExtra("trip");
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        DatabaseUtils.getUsersRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot usersnapshot: dataSnapshot.getChildren()){
                    User user = usersnapshot.getValue(User.class);
                    if(!user.getFirebaseid().equals(UserUtils.getUserId())){
                        users.add(user);
                    }
                }
                InviUserAdapter inviUserAdapter = new InviUserAdapter(ChooseInviActivity.this, users, trip);
                recyclerView.setAdapter(inviUserAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });
    }

}
