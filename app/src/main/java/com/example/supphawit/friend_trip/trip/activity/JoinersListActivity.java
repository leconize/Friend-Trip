package com.example.supphawit.friend_trip.trip.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListAdapter;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.trip.adapter.JoinerAdapter;
import com.example.supphawit.friend_trip.user.model.User;
import com.example.supphawit.friend_trip.utils.DatabaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JoinersListActivity extends AppCompatActivity {

    @BindView(R.id.joiner_recycleview)
    RecyclerView recyclerView;
    ArrayList<User> users;
    private static final String TAG = "JoinerListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joiners_list);
        ButterKnife.bind(this);
        users = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        final ArrayList<String> list = (ArrayList<String>)getIntent().getSerializableExtra("list");
        DatabaseUtils.getUsersRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot usersnapshot: dataSnapshot.getChildren()){
                    User user = usersnapshot.getValue(User.class);
                    if(list.contains(user.getFirebaseid())){
                        users.add(user);
                    }
                }
                JoinerAdapter joinerAdapter = new JoinerAdapter(JoinersListActivity.this, users);
                recyclerView.setAdapter(joinerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });
    }
}
