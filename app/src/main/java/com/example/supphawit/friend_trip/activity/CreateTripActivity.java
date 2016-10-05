package com.example.supphawit.friend_trip.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.activity.model.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateTripActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @BindView(R.id.starttime_fill) EditText starttime_fill;
    @BindView(R.id.endtime_fill) EditText endtime_fill;
    @BindView(R.id.date_fill) EditText date_fill;
    @BindView(R.id.name_fill) EditText tripname_fill;
    @BindView(R.id.place_fill) EditText place_fill;
    @BindView(R.id.submit_btn) Button submit_btn;
    @BindView(R.id.maxperson_fill) EditText maxperson_fill;
    @BindView(R.id.description_fill) EditText description_fill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        ButterKnife.bind(this);
        setTimePickerDialog(starttime_fill);
        setTimePickerDialog(endtime_fill);
        setDatePickerDialog(date_fill);
    }

    private void setDatePickerDialog(final EditText editText){
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(CreateTripActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        editText.setText(i+"/"+i1+"/"+i2);
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
        return checktextNotNull(endtime_fill) && checktextNotNull(date_fill) &&
                checktextNotNull(starttime_fill) && checktextNotNull(maxperson_fill) &&
                checktextNotNull(place_fill) && checktextNotNull(tripname_fill);
    }

    private boolean checktextNotNull(EditText editText){
        if(editText.getText().toString() != null && !editText.getText().toString().equals("")){
            return true;
        }
        return false;
    }

    @OnClick(R.id.submit_btn)
    public void submitbuttonClick(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Test");
        databaseReference.setValue("Test");
        databaseReference.push().setValue("OKIE");

        /*if(validateData() == false){
            Toast.makeText(this, "Please Fill in the blank", Toast.LENGTH_SHORT).show();
            return;
        }
        Trip trip = new Trip();
        trip.setName(tripname_fill.getText().toString());
        trip.setMaxpeople(Integer.parseInt(maxperson_fill.getText().toString()));
        trip.setStarttime(starttime_fill.getText().toString());
        trip.setEndtime(endtime_fill.getText().toString());
        trip.setPlaces(place_fill.getText().toString());
        trip.setDescription(description_fill.getText().toString());
        trip.setStartdate(date_fill.getText().toString());*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
