package com.example.supphawit.friend_trip.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.invitation.activity.RequestListActivity;
import com.example.supphawit.friend_trip.trip.activity.CreateTripActivity;
import com.example.supphawit.friend_trip.user.model.User;
import com.example.supphawit.friend_trip.utils.MyUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileActivity extends AppCompatActivity {

    private User loginuser;
    StorageReference profileRef;
    private final String TAG = "EditProfileActivity";

    @BindView(R.id.editprofile_username_fill) TextView editEmail;
    @BindView(R.id.editprofile_firstname_fill) TextView editFirstname;
    @BindView(R.id.editprofile_lastname_fill) TextView editLastname;
    @BindView(R.id.editprofile_nickname_fill) TextView editNickname;
    @BindView(R.id.editprofile_gender_spinner) Spinner editGender;
    @BindView(R.id.editprofile_mobile_fill) TextView editMobile;
    @BindView(R.id.subtoolbar)
    Toolbar toolbar;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        ButterKnife.bind(this);
        loginuser = getIntent().getParcelableExtra("loginuser");
        profileRef = FirebaseStorage.getInstance().
                getReference("profile_pic/"+loginuser.
                        getFirebaseid()+".jpg");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Male", "Female"});
        editGender.setAdapter(adapter);
        editEmail.setText(loginuser.getEmail());
        //editEmail.setKeyListener(null);
        editNickname.setText(loginuser.getNickname());
        editGender.setSelection(0);
        setSupportActionBar(toolbar);
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

    public void cancelEdit(){
        Intent intent = new Intent(this, ViewProfileActivity.class);
        intent.putExtra("loginuser", loginuser);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        MyUtils.setNotificationValue(menu);
        MenuItem item = menu.findItem(R.id.mail_noti);
        Log.i(TAG, item.toString());
        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkNotification();
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "toolbar option selected");
        switch (id) {
            case R.id.mail_noti:
                checkNotification();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void checkNotification() {
        Intent intent = new Intent(this, RequestListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.editprofile_confirm)
    public void confirmEdit(){
        Intent intent = new Intent(this, ViewProfileActivity.class);
        loginuser.setFirstname(editFirstname.getText().toString());
        loginuser.setLastname(editLastname.getText().toString());
        UserProfileChangeRequest profileupdate = new UserProfileChangeRequest.Builder().
                setDisplayName(editFirstname.getText().toString() + " " + editLastname.getText().toString()).build();
        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileupdate);
        loginuser.setNickname(editNickname.getText().toString());
        loginuser.setGender(editGender.getSelectedItem().toString());
        loginuser.setMobile(editMobile.getText().toString());
        DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference().child("users").child(loginuser.getFirebaseid());
        updateRef.setValue(loginuser);
        intent.putExtra("loginuser", loginuser);
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
