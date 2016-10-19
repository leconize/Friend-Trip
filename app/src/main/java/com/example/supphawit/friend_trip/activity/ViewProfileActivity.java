package com.example.supphawit.friend_trip.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
        //Log.d("ViewProfileActivity", loginuser.getEmail());
        final String user_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        //TODO Add real path for img file
        storageReference = FirebaseStorage.getInstance().getReference("tree.jpg");
        Log.d(TAG, "Login as " + user_email);
        FirebaseDatabase.getInstance()
                .getReference("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot usersdata) {
                        Log.d(TAG, "Query user = " + usersdata.getChildrenCount());
                        for (DataSnapshot usersnapshot : usersdata.getChildren()) {
                            User post = usersnapshot.getValue(User.class);
                            if (post.getEmail().equals(user_email)) {
                                loginuser = post;
                                setProfile(loginuser);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getMessage());
                    }
                });
        ButterKnife.bind(this);

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
        }
        if (user.getPictureurl() != null) {
            final long ONE_MEGABYTE = 1024 * 1024;
            storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    profilePicture.setImageBitmap(bmp);
                }
            });
        } else {
            Log.i(TAG, "setTodefault ProfilePicture");

        }
    }

    @OnClick(R.id.editprofilebt)
    public void editProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("loginuser", loginuser);
        startActivityForResult(intent, REQUESTCODE_EDIT);
    }

}
