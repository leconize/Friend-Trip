package com.example.supphawit.friend_trip.trip.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.invitation.activity.RequestListActivity;
import com.example.supphawit.friend_trip.other.adapter.NavigationDrawerAdapter;
import com.example.supphawit.friend_trip.trip.adapter.TripAdapter;
import com.example.supphawit.friend_trip.trip.model.Trip;
import com.example.supphawit.friend_trip.user.model.User;
import com.example.supphawit.friend_trip.utils.MyUtils;
import com.example.supphawit.friend_trip.utils.StorageUtils;
import com.example.supphawit.friend_trip.utils.UserUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripListActivity extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener{

    private static final String TAG = "TripListActivity";
    private List<Trip> trips;
    private TripAdapter tripAdapter;
    @BindView(R.id.rvTrip)
    RecyclerView recyclerView;
    @BindView(R.id.devpagetoolbar)
    Toolbar toolbar;

    private String[] mNaviTitles;
    private int mNaviIcons[] = {R.drawable.ic_list, R.drawable.ic_person, R.drawable.ic_logout};
    private ActionBarDrawerToggle mDrawerToggle;
    @BindView(R.id.trip_list_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.left_drawer)
    RecyclerView mDrawerList;
    RecyclerView.LayoutManager mLayoutManager;

    private User loginuser;
    DatabaseReference databaseReference;
    String user_id;

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

        databaseReference = FirebaseDatabase.getInstance().getReference();
        user_id = getIntent().getStringExtra("user_id");
        if (user_id == null) {
            Log.d(TAG, "cann't find user id");
            user_id = UserUtils.getUserId();
        } else {
            Log.d(TAG, user_id);
        }
        mLayoutManager = new LinearLayoutManager(this);
        mDrawerList.setLayoutManager(mLayoutManager);

        mNaviTitles = getResources().getStringArray(R.array.string_array_navi_drawer);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

                public void onDrawerClosed(View view) {
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    invalidateOptionsMenu();
                }
            };
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerToggle.syncState();
        }
        toolbar.setTitle("Trip List");
        trip_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.i(TAG, "Add trip id:" + dataSnapshot.getRef().getKey());
                Trip trip = dataSnapshot.getValue(Trip.class);
                trip.setId(dataSnapshot.getKey());
                trips.add(trip);
                tripAdapter.notifyItemInserted(trips.size() - 1);
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
                Log.e(TAG, databaseError.getCode() + " " + databaseError.getMessage());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        queryUserfromDatabase();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trip_menu_main, menu);
        MenuItem item = menu.findItem(R.id.mail_noti);
        Log.i(TAG, item.toString());
        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkNotification();
            }
        });
        MyUtils.setNotificationValue(menu);
        android.support.v7.widget.SearchView searchView = ( android.support.v7.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        Log.d(TAG, "toolbar option selected");
        switch (id) {
            case R.id.create_trip_bar:
                createTrip();
                return true;
            case R.id.mail_noti:
                checkNotification();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createTrip() {
        Intent intent = new Intent(this, CreateTripActivity.class);
        startActivity(intent);
    }

    public void checkNotification() {
        Intent intent = new Intent(this, RequestListActivity.class);
        startActivity(intent);
    }

    private void queryUserfromDatabase() {
        Log.d(TAG, "Login as " + user_id);
        databaseReference.child("users").orderByKey().equalTo(user_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot usersdata) {
                        Log.d(TAG, "Query user = " + usersdata.getChildrenCount());
                        for (DataSnapshot usersnapshot : usersdata.getChildren()) {
                            User post = usersnapshot.getValue(User.class);
                            loginuser = post;
                            mDrawerList.setAdapter(new NavigationDrawerAdapter(mNaviTitles, mNaviIcons, loginuser.getFirstname(), loginuser.getEmail()));
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getMessage());
                    }
                });
    }

    private static List<Trip> filter(List<Trip> trips, String query){
        final String lowCaseQuery = query.toLowerCase();
        Log.d(TAG, lowCaseQuery);
        final List<Trip> filterList = new ArrayList<>();
        for(Trip trip: trips){
            Log.d(TAG, trip.getName());
            final String text = trip.getName().toLowerCase();
            Log.d(TAG, text);
            if(text.contains(lowCaseQuery)){
                filterList.add(trip);
            }
        }
        return filterList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Trip> tripList = filter(trips, newText);
        tripAdapter.setTrips(tripList);
        tripAdapter.notifyDataSetChanged();
        return true;
    }
}