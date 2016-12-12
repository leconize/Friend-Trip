package com.example.supphawit.friend_trip.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.invitation.RequestModel;
import com.example.supphawit.friend_trip.invitation.activity.RequestListActivity;
import com.example.supphawit.friend_trip.trip.activity.CreateTripActivity;
import com.example.supphawit.friend_trip.user.model.User;
import com.example.supphawit.friend_trip.utils.DatabaseUtils;
import com.example.supphawit.friend_trip.utils.MyUtils;
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
import java.util.ArrayList;

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
    private boolean isProfileLoad;

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
    Toolbar toolbar;
    String user_id;
    @BindView(R.id.editprofilebt)
    Button editprofilebt;

    private StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user_id = getIntent().getStringExtra("user_id");
        if(user_id == null){
            Log.d(TAG, "cann't find user id");
            user_id = UserUtils.getUserId();
        }
        else{
            Log.d(TAG, user_id);

        }
        if(user_id.equals(UserUtils.getUserId())){
            profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectImage();
                }
            });
            editprofilebt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editProfile();
                }
            });
        }
        else{
            editprofilebt.setText("Add Friend");
            editprofilebt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addfriend();
                }
            });
        }
        isProfileLoad = false;
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        queryUserfromDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MyUtils.setNotificationValue(menu);
        MenuItem item = menu.findItem(R.id.mail_noti);
        Log.i(TAG, item.toString());
        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkNoti();
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "toolbar option selected");
        switch (id) {
            case R.id.create_trip_bar:
                createtrip();
                return true;
            case R.id.view_profile_bar:
                viewprofile();
                return true;
            case R.id.log_out_main:
                logout();
                return true;
            case R.id.mail_noti:
                checkNoti();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void viewprofile(){
        Intent intent = new Intent(this, ViewProfileActivity.class);
        startActivity(intent);
    }

    public void createtrip(){
        Intent intent = new Intent(this, CreateTripActivity.class);
        startActivity(intent);
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    public void checkNoti(){
        Intent intent = new Intent(this, RequestListActivity.class);
        startActivity(intent);
    }

    private void queryUserfromDatabase() {
        Log.d(TAG, "Login as " + user_id);
        databaseReference.child("users").orderByKey().equalTo(user_id)
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
                            if(!isProfileLoad)
                            StorageUtils.loadProfilePicture(ViewProfileActivity.this, profile_pic, user_id);
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

    public void editProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("loginuser", loginuser);
        startActivityForResult(intent, REQUESTCODE_EDIT);
    }

    public void addfriend(){
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Confirm Add Friend")
                .content("You want to Add this person to your friend list")
                .positiveText("Yes")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("invite/"+loginuser.getFirebaseid());
                        database.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<RequestModel> trips = new ArrayList<>();
                                try{
                                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                        RequestModel name = dataSnapshot1.getValue(RequestModel.class);
                                        trips.add(name);
                                    }
                                    Log.d(TAG, trips.toString());
                                    setRequestData(trips, database);
                                }
                                catch (Exception e){
                                    setRequestData(trips, database);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                ArrayList<RequestModel> trips = new ArrayList<RequestModel>();
                                setRequestData(trips, database);
                            }
                        });
                    }
                })
                .negativeText("No")
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setRequestData(final ArrayList<RequestModel> trips, final DatabaseReference database){
        DatabaseUtils.getUsersRef().orderByKey().equalTo(UserUtils.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot usersnapshot : dataSnapshot.getChildren()) {
                    User user = usersnapshot.getValue(User.class);
                    RequestModel model = new RequestModel();
                    model.setType("addfriend");
                    model.setCreator_name(user.getNickname());
                    model.setTrip_id(UserUtils.getUserId());
                    trips.add(model);
                    database.setValue(trips);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

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
                isProfileLoad = true;
            } else if (requestCode == REQUEST_SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                Glide.with(ViewProfileActivity.this).load(selectedImageUri).skipMemoryCache(true).diskCacheStrategy( DiskCacheStrategy.NONE )
                        .into(profile_pic);
                uploadfile(selectedImageUri);
                isProfileLoad = true;
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
        StorageUtils.getProfileStorageRef(UserUtils.getUserId()).putFile(imageUri).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i(TAG, "Upload success");
                        Log.i(TAG, taskSnapshot.getDownloadUrl().toString());
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
