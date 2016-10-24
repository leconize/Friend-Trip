package com.example.supphawit.friend_trip.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.model.User;
import com.example.supphawit.friend_trip.utils.DatabaseUtils;
import com.example.supphawit.friend_trip.utils.UserUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.idsignupinput) EditText idinput;
    @BindView(R.id.pwdsignupinput) EditText pwdinput;
    @BindView(R.id.nicknamesignupinput) EditText nicknameinput;

    private DatabaseReference myFirebaseRef;
    private FirebaseAuth myAuth;
    private static final String TAG = "SignUpActivity";

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
                            Toast.LENGTH_SHORT).show();//password length >= 6
                    Log.d(TAG, task.getException().toString());
                }
                else{
                    Toast.makeText(SignUpActivity.this, "You just create Account", Toast.LENGTH_SHORT).show();
                    User signupuser = new User(idinput.getText().toString(), nicknameinput.getText().toString());
                    myFirebaseRef = DatabaseUtils.getUsersRef().child(UserUtils.getUserId());
                    signupuser.setFirebaseid(UserUtils.getUserId());
                    myFirebaseRef.setValue(signupuser);
                    Log.i(TAG, "create user in database");
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick(R.id.cancelsignupbt)
    public void cancelSignUp(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }


}
