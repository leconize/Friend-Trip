package com.example.supphawit.friend_trip.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.supphawit.friend_trip.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirstPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.content_first_page);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        TextView titleFriend = (TextView)findViewById(R.id.title_friend);
        TextView titleTrip = (TextView)findViewById(R.id.title_trip);
        TextView sampleText = (TextView)findViewById(R.id.shitty_text);
        Button signIn = (Button) findViewById(R.id.signup_button);
        Button signUp = (Button) findViewById(R.id.signup_button);

        ImageView pic = (ImageView)findViewById(R.id.picpic);
        pic.setImageResource(R.drawable.user_something);

        Typeface typeCloud = Typeface.createFromAsset(getAssets(),"fonts/Cloud-Light.otf");
        Typeface typeLeela = Typeface.createFromAsset(getAssets(),"fonts/LeelawUI.ttf");

        titleFriend.setTypeface(typeCloud);
        titleTrip.setTypeface(typeCloud);
        sampleText.setTypeface(typeLeela);
        signIn.setTypeface(typeLeela);
        signUp.setTypeface(typeLeela);

    }

    @OnClick(R.id.login_button)
    public void signinbtnClick(){
        startActivity(new Intent(this, SignInActivity.class));
    }
    @OnClick(R.id.signup_button)
    public void signupbtnClick(){
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
