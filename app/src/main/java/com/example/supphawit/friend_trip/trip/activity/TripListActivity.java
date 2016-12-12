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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.util.Log;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.invitation.activity.RequestListActivity;
import com.example.supphawit.friend_trip.other.adapter.NavigationDrawerAdapter;
import com.example.supphawit.friend_trip.other.listener.DrawerItemClickListener;
import com.example.supphawit.friend_trip.trip.adapter.TripAdapter;
import com.example.supphawit.friend_trip.trip.model.Trip;
import com.example.supphawit.friend_trip.user.activity.SignInActivity;
import com.example.supphawit.friend_trip.user.activity.ViewProfileActivity;
import com.example.supphawit.friend_trip.user.model.User;
import com.example.supphawit.friend_trip.utils.StorageUtils;
import com.example.supphawit.friend_trip.utils.UserUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class TripListActivity extends AppCompatActivity{

    private static final String TAG = "TripListActivity";
    private List<Trip> trips;
    private TripAdapter tripAdapter;
    @BindView(R.id.rvTrip) RecyclerView recyclerView;
    @BindView(R.id.devpagetoolbar) Toolbar toolbar;

    private String[] mNaviTitles;
    private int mNaviIcons[] = {R.drawable.ic_list};
    private ActionBarDrawerToggle mDrawerToggle;
    @BindView(R.id.trip_list_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.left_drawer)  RecyclerView mDrawerList;
    RecyclerView.LayoutManager mLayoutManager;

    private User loginuser;
    DatabaseReference databaseReference;
    String user_id;
    private StorageReference storageReference;
    CircleImageView profile_pic;

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
        if(user_id == null){
            Log.d(TAG, "cann't find user id");
            user_id = UserUtils.getUserId();
        }
        else{
            Log.d(TAG, user_id);
        }
        mLayoutManager = new LinearLayoutManager(this);
        mDrawerList.setLayoutManager(mLayoutManager);

        mNaviTitles = getResources().getStringArray(R.array.string_array_navi_drawer);
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mNaviTitles));
        //mDrawerList.setOnClickListener(new DrawerItemClickListener());
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            {

                public void onDrawerClosed(View view)
                {
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    //drawerOpened = false;
                }

                public void onDrawerOpened(View drawerView)
                {
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    //drawerOpened = true;
                }
            };
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerToggle.syncState();
        }

        //searchView.setOnQueryTextListener(this);
        //toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("Trip List");
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

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
                            storageReference = FirebaseStorage.getInstance().
                                    getReference("profile_pic/" + loginuser.getFirebaseid()
                                            + ".jpg");
                            mDrawerList.setAdapter(new NavigationDrawerAdapter(mNaviTitles, mNaviIcons, loginuser.getFirstname(), loginuser.getEmail()));
                            profile_pic = (CircleImageView) findViewById(R.id.circleView);
                            StorageUtils.loadProfilePicture(TripListActivity.this, profile_pic, user_id);
                            return;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getMessage());
                    }
                });
    }
}
