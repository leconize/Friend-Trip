package com.example.supphawit.friend_trip.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.model.LoginInteractor;
import com.example.supphawit.friend_trip.model.User;
import com.example.supphawit.friend_trip.presenter.LoginPresenter;
import com.example.supphawit.friend_trip.utils.UserUtils;
import com.example.supphawit.friend_trip.view.LoginView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity implements LoginView {

    private DatabaseReference myFirebaseRef;
    private FirebaseAuth myAuth;
    private static final String TAG = "SignInActivity";

    @BindView(R.id.idinput) EditText idinput;
    @BindView(R.id.pwdinput) EditText pwdinput;
    @BindView(R.id.devpagetoolbar) Toolbar devtoolbar;
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        devtoolbar.setTitle("Login");
        setSupportActionBar(devtoolbar);
        if(UserUtils.isUserInAuth()){
            Intent intent = new Intent(SignInActivity.this, DeveloperActivity.class);
            startActivity(intent);
            finish();
        }
        loginPresenter = new LoginPresenter(new LoginInteractor());
        loginPresenter.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.unbind();
    }

    @Override
    public void signinSuccess() {
        Log.i(TAG, "Login Success Start new Activity");
        startActivity(new Intent(this, DeveloperActivity.class));
        finish();
    }

    @Override
    public void sigininFail() {
        Log.i(TAG, "Fail to LogIn");
        Toast.makeText(this, "Auth Fail", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void warnEmptyInput() {
        Toast.makeText(this, "Please Fill all Text Field", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.loginbt)
    public void loginBtnClick(){
        loginPresenter.login(idinput.getText().toString(), pwdinput.getText().toString());
    }
}