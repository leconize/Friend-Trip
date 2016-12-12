package com.example.supphawit.friend_trip.user.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.other.activity.DeveloperActivity;
import com.example.supphawit.friend_trip.trip.activity.TripListActivity;
import com.example.supphawit.friend_trip.user.model.LoginInteractor;
import com.example.supphawit.friend_trip.user.presenter.LoginPresenter;
import com.example.supphawit.friend_trip.utils.UserUtils;
import com.example.supphawit.friend_trip.user.view.LoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity implements LoginView {

    private static final String TAG = "SignInActivity";

    @BindView(R.id.idinput)public  EditText idinput;
    @BindView(R.id.pwdinput)public  EditText pwdinput;
    @BindView(R.id.subtoolbar) Toolbar devtoolbar;
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        devtoolbar.setTitle("Login");
        setSupportActionBar(devtoolbar);
        if(UserUtils.isUserInAuth()){
            Intent intent = new Intent(SignInActivity.this, TripListActivity.class);
            startActivity(intent);
            finish();
        }
        loginPresenter = new LoginPresenter(new LoginInteractor());
        loginPresenter.bind(this);
    }

    public void setLoginPresenter(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.unbind();
    }

    @Override
    public void signinSuccess() {
        Log.i(TAG, "Login Success Start new Activity");
        startActivity(new Intent(this, TripListActivity.class));
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