package com.example.supphawit.friend_trip.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.listener.FirebaseCompleteListener;
import com.example.supphawit.friend_trip.model.Trip;
import com.example.supphawit.friend_trip.utils.DatabaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @BindArray(R.array.string_array_name) String[] autocompleteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_create_trip);
        ButterKnife.bind(this);
        setTimePickerDialog(starttime_fill);
        setTimePickerDialog(endtime_fill);
        setDatePickerDialog(startdate_fill);
        setDatePickerDialog(enddate_fill);
    }


    @OnClick(R.id.submit_btn)
    public void submitButtonClick(){
        if(!validateData()){
            Toast.makeText(this, "Please Enter all value", Toast.LENGTH_SHORT).show();
        }
        else{
            try{
                List<String> places = getPlacesValues();
                Trip trip = createTrip(places);
                FirebaseDatabase.getInstance().getReference("trips").push().setValue(trip);
                Toast.makeText(this, "Trip created", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateTripActivity.this, TripListActivity.class);
                startActivity(intent);
            }
            catch (NullPointerException e){
                Log.d(TAG, "get null value from place edittexts");
                Toast.makeText(this, "Please Enter all value", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.addplace_btn)
    public void makePlaceEditText(){
        AutoCompleteTextView editText = new AutoCompleteTextView(this);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, autocompleteData);
        editText.setAdapter(adapter);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.place_wrapper);
        linearLayout.addView(editText);
    }

    private List<String> getPlacesValues() throws NullPointerException{
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.place_wrapper);
        final int childcount = linearLayout.getChildCount();
        ArrayList<String> places = new ArrayList<>();
        for(int i = 0; i < childcount; i++){
            AutoCompleteTextView textView = (AutoCompleteTextView) linearLayout.getChildAt(i);
            places.add(textView.getText().toString());
            if(places.get(i) == null || places.get(i).equals("")){
                throw new NullPointerException();
            }
        }
        return places;
    }

    private Trip createTrip(List<String> places){
        Trip trip = createTripWithoutPlaces();
        trip.setPlaces(places);
        return trip;
    }


    private Trip createTripWithoutPlaces() {
        Trip trip = new Trip();
        trip.setName(tripname_fill.getText().toString());
        trip.setMaxpeople(Integer.parseInt(maxperson_fill.getText().toString()));
        trip.setStartdate(startdate_fill.getText().toString());
        trip.setEnddate(enddate_fill.getText().toString());
        trip.setStarttime(starttime_fill.getText().toString());
        trip.setEndtime(endtime_fill.getText().toString());
        trip.setCreaterid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        trip.setCreatername(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
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
                checktextNotNull(tripname_fill);
    }

    private boolean checktextNotNull(EditText editText){
        return !editText.getText().toString().equals("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
