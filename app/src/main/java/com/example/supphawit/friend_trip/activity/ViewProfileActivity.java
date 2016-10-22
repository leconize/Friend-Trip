package com.example.supphawit.friend_trip.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewProfileActivity extends AppCompatActivity {

    private User loginuser;
    private final String TAG = "ViewProfileActivity";
    private static final int REQUESTCODE_EDIT = 159;
    DatabaseReference databaseReference;

    @BindView(R.id.profile_picture)
    ImageView profilePicture;
    @BindView(R.id.viewemail)
    TextView profileEmail;
    @BindView(R.id.viewfirstname)
    TextView profileFirstname;
    @BindView(R.id.viewlastname)
    TextView profileLastname;
    @BindView(R.id.viewnickname)
    TextView profileNickname;
    @BindView(R.id.viewgender)
    TextView profileGender;
    @BindView(R.id.viewmobile)
    TextView profileMobile;

    private StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        ButterKnife.bind(this);
        queryUserfromDatabase();
        loadPicture();
    }

    private void loadPicture() {
        databaseReference.child("profile_pic/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i(TAG, dataSnapshot.toString());
                        if (dataSnapshot.exists()) {
                            Log.i(TAG, "start downloading");
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(ViewProfileActivity.this).load(uri).into(profilePicture);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, e.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void queryUserfromDatabase() {
        final String user_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Log.d(TAG, "Login as " + user_email);
        databaseReference.child("users").orderByChild("email").equalTo(user_email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot usersdata) {
                        Log.d(TAG, "Query user = " + usersdata.getChildrenCount());
                        for (DataSnapshot usersnapshot : usersdata.getChildren()) {
                            User post = usersnapshot.getValue(User.class);
                            loginuser = post;
                            storageReference = FirebaseStorage.getInstance().
                                    getReference("profile_pic/" + loginuser.getFirebaseid()
                                            + ".jpg");
                            setProfile(loginuser);
                            return;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getMessage());
                    }
                });
    }

    private void setProfile(User user) {
        profileEmail.setText(user.getEmail());
        profileNickname.setText(user.getNickname());
        if (user.getFirstname() != null) {
            profileFirstname.setText(user.getFirstname());
        }
        if (user.getLastname() != null) {
            profileLastname.setText(user.getLastname());
        }
        if (user.getGender() != null) {
            profileGender.setText(user.getGender());
        }
        if (user.getMobile() != null) {
            profileMobile.setText(user.getMobile());
        } else {
            Log.i(TAG, "set To default ProfilePicture");

        }
    }

    @OnClick(R.id.editprofilebt)
    public void editProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("loginuser", loginuser);
        startActivityForResult(intent, REQUESTCODE_EDIT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
