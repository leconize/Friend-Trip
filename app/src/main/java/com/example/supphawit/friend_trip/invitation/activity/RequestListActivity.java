package com.example.supphawit.friend_trip.invitation.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.invitation.RequestModel;
import com.example.supphawit.friend_trip.trip.activity.CreateTripActivity;
import com.example.supphawit.friend_trip.trip.activity.NewTripDetailActivity;
import com.example.supphawit.friend_trip.trip.model.Trip;
import com.example.supphawit.friend_trip.user.activity.SignInActivity;
import com.example.supphawit.friend_trip.user.activity.ViewProfileActivity;
import com.example.supphawit.friend_trip.user.model.User;
import com.example.supphawit.friend_trip.utils.DatabaseUtils;
import com.example.supphawit.friend_trip.utils.MyUtils;
import com.example.supphawit.friend_trip.utils.UserUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestListActivity extends AppCompatActivity {

    @BindView(R.id.request_list)
    ListView recyclerView;
    ArrayList<String> requestList;
    private static final String TAG = "RequestListActivtity";
    ArrayList<RequestModel> requestModels;
    @BindView(R.id.devpagetoolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        ButterKnife.bind(this);
        requestList = new ArrayList<>();
        requestModels = new ArrayList<>();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("invite/"+ UserUtils.getUserId());
        setSupportActionBar(toolbar);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot request : dataSnapshot.getChildren()){
                    RequestModel requestModel = request.getValue(RequestModel.class);
                    requestModels.add(requestModel);
                    if(requestModel.getType().equals("addfriend")){
                        requestList.add("You have friend request from " +requestModel.getCreator_name());
                    }
                    else{
                        requestList.add("You have request from " +requestModel.getCreator_name());
                    }

                }
                ArrayAdapter<String> t = new ArrayAdapter<>(RequestListActivity.this, android.R.layout.simple_list_item_1, requestList);
                recyclerView.setAdapter(t);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });
        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if(requestModels.get(i).getType().equals("addfriend")){
                    setFriendAddAction(databaseReference, i);
                }
                else{
                    setTripInviteAction(databaseReference, i);
                }
            }
        });

    }

    public void setFriendAddAction(final DatabaseReference databaseReference,final int i){
        final String user_id = requestModels.get(i).getTrip_id();
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Confirm Add Friend")
                .content("You want to Add this person to your friend list")
                .positiveText("Yes")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("friend/"+UserUtils.getUserId());
                        database.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<String> friend_list = new ArrayList<>();
                                try{
                                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                        String name = dataSnapshot1.getValue(String.class);
                                        friend_list.add(name);
                                    }
                                    Log.d(TAG, friend_list.toString());
                                    friend_list.add(user_id);
                                    database.setValue(friend_list);
                                    databaseReference.child(i+"").removeValue();
                                }
                                catch (Exception e){
                                    friend_list.add(user_id);
                                    database.setValue(friend_list);
                                    databaseReference.child(i+"").removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                ArrayList<String> friend_list = new ArrayList<>();
                                friend_list.add(user_id);
                                database.setValue(friend_list);
                                databaseReference.child(i+"").removeValue();
                            }
                        });
                    }
                })
                .negativeText("No")
                .show();
    }

    public void setTripInviteAction(final DatabaseReference databaseReference,final int i){
        DatabaseUtils.getTripRef(requestModels.get(i).getTrip_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Trip trip = dataSnapshot.getValue(Trip.class);
                Intent intent = new Intent(RequestListActivity.this, NewTripDetailActivity.class);
                intent.putExtra("trip", trip);
                startActivity(intent);
                Log.d(TAG, dataSnapshot.getRef().toString());
                databaseReference.child(i+"").removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MyUtils.setNotificationValue(menu);
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
        finish();
    }
}
