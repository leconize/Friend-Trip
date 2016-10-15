package com.example.supphawit.friend_trip.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.model.User;
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

public class SignInActivity extends AppCompatActivity {

    private DatabaseReference myFirebaseRef;
    private FirebaseAuth myAuth;
    private FirebaseAuth.AuthStateListener myAuthListener;
    private static final String TAG = "SignInActivity";

    @BindView(R.id.idinput) EditText idinput;
    @BindView(R.id.pwdinput) EditText pwdinput;
    public SignInActivity() {
    }

    private void createAuthUser() {
        myAuth = FirebaseAuth.getInstance();
        myAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private void createDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myFirebaseRef = database.getReference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        createAuthUser();
        createDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try{
            if(FirebaseAuth.getInstance().getCurrentUser() != null){
                Intent intent = new Intent(this, DeveloperActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e){//Null FirebaseAuth
            Log.e(TAG, e.getMessage());
            Log.e(TAG, "Error cann't get firebaseAuth");
        }
        //myAuth.addAuthStateListener(myAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //myAuth.removeAuthStateListener(myAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            String getmytext = data.getStringExtra("idsignup");
            Log.d(TAG, getmytext);
        }
    }

    private boolean validateinput(){
        return checktextNotNull(pwdinput) && checktextNotNull(idinput);
    }

    private boolean checktextNotNull(EditText editText){
        if(editText.getText().toString() != null && !editText.getText().toString().equals("")){
            return true;
        }
        return false;
    }

    @OnClick(R.id.loginbt)
    public void loginbtnClick(){
        if(!validateinput()){
            Toast.makeText(this, "please fill in all field", Toast.LENGTH_SHORT).show();
            return;
        }
        myAuth.signInWithEmailAndPassword(idinput.getText().toString(),
                pwdinput.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            final String useremail = myAuth.getCurrentUser().getEmail();
                            myFirebaseRef = FirebaseDatabase.getInstance().getReference().child("users");
                            //Log.d(TAG, myFirebaseRef.toString());
                            myFirebaseRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //Log.e("Count ",""+dataSnapshot.getChildrenCount());
                                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                            User post = postSnapshot.getValue(User.class);
                                        if(post.getEmail().equals(useremail)){
                                            //Log.e("Get data", post.getEmail());
                                            Intent intent = new Intent(SignInActivity.this, DeveloperActivity.class);
                                            intent.putExtra("loginuser", post);
                                            startActivity(intent);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("Read failed: ", databaseError.getMessage());
                                }
                            });
                        }
                    }
                });
    }

    @OnClick(R.id.tosignupbt)
    public void toSignUp(View view){
        Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivityForResult(i, 1);
    }
}
