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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.supphawit.friend_trip.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateTripActivity extends AppCompatActivity {

    @BindView(R.id.start_date_fill) EditText startdate_fill;
    @BindView(R.id.start_time_fill) EditText starttime_fill;
    @BindView(R.id.end_time_fill) EditText endtime_fill;
    @BindView(R.id.end_date_fill) EditText enddate_fill;
    @BindView(R.id.name_fill) EditText tripname_fill;
    @BindView(R.id.submit_btn) Button submit_btn;
    @BindView(R.id.maxpeople_fill) EditText maxperson_fill;

    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        ButterKnife.bind(this);
        setTimePickerDialog(starttime_fill);
       setTimePickerDialog(endtime_fill);
        setDatePickerDialog(startdate_fill);
        setDatePickerDialog(enddate_fill);


    }
    @OnClick(R.id.addplace_btn)
    public void makePlaceEditText(){
        AutoCompleteTextView editText = new AutoCompleteTextView(this);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        editText.setAdapter(adapter);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.place_wrapper);
        linearLayout.addView(editText);
    }

    @OnClick(R.id.submit_btn)
    public void createButtonClick(){
        if(!validateData()){
            Toast.makeText(this, "Please Enter all value", Toast.LENGTH_SHORT).show();
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.place_wrapper);
            final int childcount = linearLayout.getChildCount();
            for(int i = 0; i < childcount; i++){
                AutoCompleteTextView textView = (AutoCompleteTextView) linearLayout.getChildAt(i);
                textView.getText().toString();
            }
        }
        else{

        }
    }

    private void setDatePickerDialog(final EditText editText){
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = editText.getText().toString();
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
        if(editText.getText().toString() != null && !editText.getText().toString().equals("")){
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
