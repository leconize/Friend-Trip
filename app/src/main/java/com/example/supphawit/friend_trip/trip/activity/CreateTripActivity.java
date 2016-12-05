package com.example.supphawit.friend_trip.trip.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.trip.model.Trip;
import com.example.supphawit.friend_trip.user.activity.SignInActivity;
import com.example.supphawit.friend_trip.user.model.User;
import com.example.supphawit.friend_trip.utils.DatabaseUtils;
import com.example.supphawit.friend_trip.utils.UserUtils;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateTripActivity extends AppCompatActivity {

    private static final String TAG = CreateTripActivity.class.getName();
    FirebaseAuth firebaseAuth;

    @BindView(R.id.start_date_fill) EditText startdate_fill;
    @BindView(R.id.start_time_fill) EditText starttime_fill;
    @BindView(R.id.end_time_fill) EditText endtime_fill;
    @BindView(R.id.end_date_fill) EditText enddate_fill;
    @BindView(R.id.name_fill) EditText tripname_fill;
    @BindView(R.id.submit_btn) Button submit_btn;
    @BindView(R.id.maxpeople_fill) EditText maxperson_fill;
    @BindView(R.id.devpagetoolbar) Toolbar devtoolbar;
    @BindView(R.id.addplace_btn) EditText meetingPoint;
    @BindView(R.id.create_des_fill) EditText description;
    @BindView(R.id.tag_edit) AutoCompleteTextView tagadd;
    @BindView(R.id.tag_group)
    TagView tagView;


    private int PLACE_PICKER_REQUEST = 1;
    private LatLng latLng;


    @BindArray(R.array.string_array_name) String[] autocompleteData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_create_trip);
        ButterKnife.bind(this);
        tagadd.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, autocompleteData));
        tagadd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionid, KeyEvent keyEvent) {
                if(actionid == EditorInfo.IME_ACTION_DONE){
                    String tagvalue = tagadd.getText().toString();
                    if(!tagvalue.equals("")){
                        Tag tag = new Tag(tagadd.getText().toString());
                        tag.isDeletable = true;
                        tagView.addTag(tag);
                        tagadd.setText("");
                    }

                    return true;
                }
                return false;
            }
            ;
        });
        setSupportActionBar(devtoolbar);
        setTimePickerDialog(starttime_fill);
        setTimePickerDialog(endtime_fill);
        setDatePickerDialog(startdate_fill);
        setDatePickerDialog(enddate_fill);
        tagView.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(TagView tagView, Tag tag, int i) {
                tagView.remove(i);
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


    @OnClick(R.id.submit_btn)
    public void submitButtonClick(){
        if(!validateData()){
            Toast.makeText(this, "Please Enter all value", Toast.LENGTH_SHORT).show();
        }
        else{
            try{
                final Trip trip = createTrip();
                DatabaseUtils.getUsersRef().orderByKey().equalTo(UserUtils.getUserId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot usersdata) {
                                Log.d(TAG, "Query user = " + usersdata.getChildrenCount());
                                for (DataSnapshot usersnapshot : usersdata.getChildren()) {
                                    User post = usersnapshot.getValue(User.class);
                                    User loginuser = post;
                                    trip.setCreatername(loginuser.getNickname());
                                    FirebaseDatabase.getInstance().getReference("trips").push().setValue(trip);
                                    Toast.makeText(CreateTripActivity.this, "Trip created", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CreateTripActivity.this, TripListActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return;
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d(TAG, databaseError.getMessage());
                            }
                        });
            }
            catch (NullPointerException e){
                Log.d(TAG, "get null value from place edittexts");
                Toast.makeText(this, "Please Enter all value", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @OnClick(R.id.addplace_btn)
    public void gotoMarker(){

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try{
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                latLng = place.getLatLng();
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                meetingPoint.setText(place.getName());
            }
        }
    }


    private Trip createTrip() {
        ArrayList<String> joiner_list = new ArrayList<>();
        joiner_list.add(UserUtils.getUserId());
        Trip trip = new Trip();
        trip.setName(tripname_fill.getText().toString());
        trip.setMaxpeople(Integer.parseInt(maxperson_fill.getText().toString()));
        trip.setStartdate(startdate_fill.getText().toString());
        trip.setEnddate(enddate_fill.getText().toString());
        trip.setStarttime(starttime_fill.getText().toString());
        trip.setEndtime(endtime_fill.getText().toString());
        trip.setCreaterid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        trip.setCreatername(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        trip.setLatitude(latLng.latitude);
        trip.setLongitude(latLng.longitude);
        trip.setDescription(description.getText().toString());
        trip.setJoinerId_list(joiner_list);
        ArrayList<String> tagslist=  new ArrayList<>();
        for(Tag tag: tagView.getTags()){
            tagslist.add(tag.text);
        }
        trip.setTags(tagslist);
        return trip;
    }

    private void setDatePickerDialog(final EditText editText){
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(CreateTripActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        editText.setText(day+"/"+month+"/"+year);
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }
    private void setTimePickerDialog(final EditText editText){
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editText.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }

    private boolean validateData(){
        return checktextNotNull(endtime_fill) && checktextNotNull(startdate_fill) &&
                checktextNotNull(starttime_fill) && checktextNotNull(maxperson_fill) &&
                checktextNotNull(tripname_fill) && latLng != null && checktextNotNull(description);
    }

    private boolean checktextNotNull(EditText editText){
        return !editText.getText().toString().equals("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
