package com.example.supphawit.friend_trip.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileActivity extends AppCompatActivity {

    private User loginuser;
    private final String TAG = "EditProfileActivity";

    @BindView(R.id.editemail) TextView editEmail;
    @BindView(R.id.editfirstname) TextView editFirstname;
    @BindView(R.id.editlastname) TextView editLastname;
    @BindView(R.id.editnickname) TextView editNickname;
    @BindView(R.id.editgender) Spinner editGender;
    @BindView(R.id.editmobile) TextView editMobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        ButterKnife.bind(this);
        loginuser = getIntent().getParcelableExtra("loginuser");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Male", "Female"});
        editGender.setAdapter(adapter);
        editEmail.setText(loginuser.getEmail());
        //editEmail.setKeyListener(null);
        editNickname.setText(loginuser.getNickname());
        editGender.setSelection(0);
        if(loginuser.getFirstname() != null) {
            editFirstname.setText(loginuser.getFirstname());
        }
        if(loginuser.getLastname() != null) {
            editLastname.setText(loginuser.getLastname());
        }
        if(loginuser.getMobile() != null) {
            editMobile.setText(loginuser.getMobile());
        }
    }

    @OnClick(R.id.closeeditdialog)
    public void cancelEdit(){
        Intent intent = new Intent(this, ViewProfileActivity.class);
        intent.putExtra("loginuser", loginuser);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @OnClick(R.id.confirmeditdialog)
    public void confirmEdit(){
        Intent intent = new Intent(this, ViewProfileActivity.class);
        loginuser.setFirstname(editFirstname.getText().toString());
        loginuser.setLastname(editLastname.getText().toString());
        loginuser.setNickname(editNickname.getText().toString());
        loginuser.setGender(editGender.getSelectedItem().toString());
        loginuser.setMobile(editMobile.getText().toString());
        DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference().child("users").child(loginuser.getFirebaseid());
        updateRef.setValue(loginuser);
        intent.putExtra("loginuser", loginuser);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
