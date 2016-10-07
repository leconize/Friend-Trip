package com.example.supphawit.friend_trip.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.supphawit.friend_trip.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.idsignupinput) EditText idinput;
    @BindView(R.id.pwdsignupinput) EditText pwdinput;

    public static final int REQ_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }

    private boolean validateData(){
        return checktextNotNull(idinput) && checktextNotNull(pwdinput);
    }

    private boolean checktextNotNull(EditText editText){
        if(editText.getText().toString() != null && !editText.getText().toString().equals("")){
            return true;
        }
        return false;
    }

    @OnClick(R.id.signupbtn)
    public void signupbtnClick(){
        if(!validateData()){
            Toast.makeText(this, "Please Enter Email & Password", Toast.LENGTH_SHORT).show();
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(idinput.getText().toString(),
                pwdinput.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();//paasword length >= 6
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Great Job mate", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }
        });

    }



}