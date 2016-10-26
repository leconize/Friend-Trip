package com.example.supphawit.friend_trip.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.model.User;
import com.example.supphawit.friend_trip.utils.DatabaseUtils;
import com.example.supphawit.friend_trip.utils.StorageUtils;
import com.example.supphawit.friend_trip.utils.UserUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewProfileActivity extends AppCompatActivity {

    private User loginuser;
    private final String TAG = "ViewProfileActivity";
    private static final int REQUESTCODE_EDIT = 159;
    DatabaseReference databaseReference;

    private final String CAMERA_IMG_NAME = "photo.jpg";
    public final String APP_TAG = "FRIEND_TRIP";
    private final int REQUEST_CAMERA = 269;
    private final int REQUEST_SELECT_FILE = 369;

    @BindView(R.id.profile_email_fill)
    TextView profileEmail;
    @BindView(R.id.profile_name)
    TextView profileName;
    @BindView(R.id.profile_username)
    TextView profileNickname;
    @BindView(R.id.profile_gender_fill)
    TextView profileGender;
    @BindView(R.id.profile_contract_fill)
    TextView profileMobile;
    @BindView(R.id.profile_userpic)
    ImageView profile_pic;
    @BindView(R.id.devpagetoolbar)
    Toolbar devtoolbar;

    private StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        ButterKnife.bind(this);
        setSupportActionBar(devtoolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        queryUserfromDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "toolbar option selected");
        switch (id) {
            case R.id.log_out_main:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, SignInActivity.class));
    }

    private void queryUserfromDatabase() {
        final String user_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Log.d(TAG, "Login as " + user_email);
        databaseReference.child("users").orderByKey().equalTo(UserUtils.getUserId())
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
                            loadPicture();
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

    private void loadPicture() {
        Log.i(TAG, "Load Picture function start");
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
                                    try{
                                        Glide.with(ViewProfileActivity.this).load(uri).override(100,100).into(profile_pic);
                                    }
                                    catch (Exception e){
                                        Log.e(TAG, e.getMessage());
                                    }
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
        Log.i(TAG, databaseReference.toString());
    }

    private void setProfile(User user) {
        profileEmail.setText(user.getEmail());
        profileNickname.setText(user.getNickname());
        if (user.getFirstname() != null) {
            profileName.setText(user.getFirstname());
        }
        if (user.getLastname() != null) {
            profileName.setText(profileName.getText().toString()+" " + user.getLastname());
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
        Glide.clear(profile_pic);
    }

    @OnClick(R.id.profile_userpic)
    public void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Log.i(TAG, "Take Photo");
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(CAMERA_IMG_NAME));
                    if(intent.resolveActivity(getPackageManager()) != null){
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }
                } else if (items[item].equals("Choose from Library")) {
                    Log.i(TAG, "Choose from Library");
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            REQUEST_SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public Uri getPhotoFileUri(String fileName) {
        if (isExternalStorageAvailable()) {
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                takePhotoAction();
            } else if (requestCode == REQUEST_SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                Glide.with(ViewProfileActivity.this).load(selectedImageUri).skipMemoryCache(true).diskCacheStrategy( DiskCacheStrategy.NONE )
                        .into(profile_pic);
                uploadfile(selectedImageUri);
            }
        }
    }

    private void takePhotoAction(){
        Uri takenPhotoUri = getPhotoFileUri(CAMERA_IMG_NAME);
        Glide.with(ViewProfileActivity.this).load(takenPhotoUri).skipMemoryCache(true).diskCacheStrategy( DiskCacheStrategy.NONE )
                .into(profile_pic);
        uploadfile(takenPhotoUri);
    }

    private void uploadfile(final Uri imageUri){
        Log.i(TAG, "StartUploading File");
        Log.i(TAG, "New Profile Picture");

        StorageUtils.getProfileStorageRef(UserUtils.getUserId()).putFile(imageUri).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i(TAG, "Upload success");
                        Log.i(TAG, taskSnapshot.getDownloadUrl().toString());
                        Toast.makeText(ViewProfileActivity.this, "Upload success", Toast.LENGTH_SHORT);
                        UserProfileChangeRequest profileupdate = new UserProfileChangeRequest.Builder().
                                setPhotoUri(taskSnapshot.getDownloadUrl()).build();
                        DatabaseReference updateRef = DatabaseUtils.getUserProfileRef()
                                .child(loginuser.getFirebaseid());
                        updateRef.setValue(true);


                        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User Profile updated.");
                                }
                                else{
                                    Log.d(TAG, "Fail to UpdateUser Profile");
                                }
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewProfileActivity.this, "Please Try again", Toast.LENGTH_SHORT);
                        loginuser.setPictureurl("false");
                        Log.i(TAG, "Upload fail");
                    }
                });
    }
}
