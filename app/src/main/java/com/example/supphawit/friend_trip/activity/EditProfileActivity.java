package com.example.supphawit.friend_trip.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.model.User;
import com.example.supphawit.friend_trip.utils.DatabaseUtils;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileActivity extends AppCompatActivity {

    private User loginuser;
    StorageReference profileRef;
    public final String APP_TAG = "FRIEND_TRIP";
    private final String TAG = "EditProfileActivity";
    private final String CAMERA_IMG_NAME = "photo.jpg";

    private final int REQUEST_CAMERA = 269;
    private final int REQUEST_SELECT_FILE = 369;

    @BindView(R.id.editprofile_picture) ImageView editprofile_picture;
    @BindView(R.id.editemail) TextView editEmail;
    @BindView(R.id.editfirstname) TextView editFirstname;
    @BindView(R.id.editlastname) TextView editLastname;
    @BindView(R.id.editnickname) TextView editNickname;
    @BindView(R.id.editgender) Spinner editGender;
    @BindView(R.id.editmobile) TextView editMobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        ButterKnife.bind(this);
        loginuser = getIntent().getParcelableExtra("loginuser");
        profileRef = FirebaseStorage.getInstance().
                getReference("profile_pic/"+loginuser.
                        getFirebaseid()+".jpg");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Male", "Female"});
        editGender.setAdapter(adapter);
        editEmail.setText(loginuser.getEmail());
        //editEmail.setKeyListener(null);
        editNickname.setText(loginuser.getNickname());
        editGender.setSelection(0);
        if(loginuser.getFirstname() != null) {
            editFirstname.setText(loginuser.getFirstname());
        }
        if(loginuser.getLastname() != null) {
            editLastname.setText(loginuser.getLastname());
        }
        if(loginuser.getMobile() != null) {
            editMobile.setText(loginuser.getMobile());
        }
        loadPicture();
    }

    private void loadPicture() {
        FirebaseDatabase.getInstance().getReference()
                .child("profile_pic/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i(TAG, dataSnapshot.toString());
                        if (dataSnapshot.exists()) {
                            Log.i(TAG, "start download Profile Picture");
                            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(EditProfileActivity.this)
                                            .load(uri)
                                            .override(80,80)
                                            .into(editprofile_picture);
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
                        Log.d(TAG, databaseError.getMessage());
                    }
                });
    }



    @OnClick(R.id.editprofile_picture)
    public void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
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
                uploadfile(selectedImageUri);
                Glide.with(this).load(selectedImageUri).into(editprofile_picture);
            }
        }
    }

    private void takePhotoAction(){
        Uri takenPhotoUri = getPhotoFileUri(CAMERA_IMG_NAME);
        Glide.with(this).load(takenPhotoUri).skipMemoryCache(true).diskCacheStrategy( DiskCacheStrategy.NONE )
                .into(editprofile_picture);
        uploadfile(takenPhotoUri);


    }

    private void uploadfile(Uri imageUri){
        Log.i(TAG, "StartUploading File");
        profileRef.putFile(imageUri).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i(TAG, "Upload success");
                        Log.i(TAG, taskSnapshot.getDownloadUrl().toString());
                        Toast.makeText(EditProfileActivity.this, "Upload success", Toast.LENGTH_SHORT);
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
                        Toast.makeText(EditProfileActivity.this, "Please Try again", Toast.LENGTH_SHORT);
                        loginuser.setPictureurl("false");
                        Log.i(TAG, "Upload fail");
                    }
                });
    }


    @OnClick(R.id.closeeditdialog)
    public void cancelEdit(){
        Intent intent = new Intent(this, ViewProfileActivity.class);
        intent.putExtra("loginuser", loginuser);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @OnClick(R.id.confirmeditdialog)
    public void confirmEdit(){
        Intent intent = new Intent(this, ViewProfileActivity.class);
        loginuser.setFirstname(editFirstname.getText().toString());
        loginuser.setLastname(editLastname.getText().toString());
        loginuser.setNickname(editNickname.getText().toString());
        loginuser.setGender(editGender.getSelectedItem().toString());
        loginuser.setMobile(editMobile.getText().toString());
        DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference().child("users").child(loginuser.getFirebaseid());
        updateRef.setValue(loginuser);
        intent.putExtra("loginuser", loginuser);
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Glide.clear(editprofile_picture);
    }
}
