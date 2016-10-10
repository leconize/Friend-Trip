package com.example.supphawit.friend_trip.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewProfileActivity extends AppCompatActivity{

    private User loginuser;
    private final String TAG = "ViewProfileActivity";
    private static final int REQUESTCODE_EDIT = 159;

    @BindView(R.id.viewemail) TextView profileEmail;
    @BindView(R.id.viewfirstname) TextView profileFirstname;
    @BindView(R.id.viewlastname) TextView profileLastname;
    @BindView(R.id.viewnickname) TextView profileNickname;
    @BindView(R.id.viewgender) TextView profileGender;
    @BindView(R.id.viewmobile) TextView profileMobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        loginuser = getIntent().getParcelableExtra("loginuser");
        //Log.d("ViewProfileActivity", loginuser.getEmail());
        ButterKnife.bind(this);
        setProfile(loginuser);
    }

    private void setProfile (User user){
        profileEmail.setText(user.getEmail());
        profileNickname.setText(user.getNickname());
        if(user.getFirstname() != null){
            profileFirstname.setText(user.getFirstname());
        }
        if(user.getLastname() != null){
            profileLastname.setText(user.getLastname());
        }
        if(user.getGender() != null){
            profileGender.setText(user.getGender());
        }
        if(user.getMobile() != null){
            profileMobile.setText(user.getMobile());
        }
    }

    @OnClick(R.id.editprofilebt)
    public void editProfile(){
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("loginuser", loginuser);
        startActivityForResult(intent, REQUESTCODE_EDIT);
    }

}
