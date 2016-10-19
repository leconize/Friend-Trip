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

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    StorageReference storageReference;
    private final String TAG = "EditProfileActivity";
    private final String CAMERA_IMG_NAME = "temp.jpg";

    private final int REQUEST_CAMERA = 269;
    private final int SELECT_FILE = 369;

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
        storageReference = FirebaseStorage.getInstance().
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
        if(loginuser.getPictureurl() != null){
            final long ONE_MEGABYTE = 1024 * 1024;
            storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    editprofile_picture.setImageBitmap(bmp);
                }
            });
        }
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
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), CAMERA_IMG_NAME);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                takePhotoAction();
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                uploadfile(selectedImageUri);
                String tempPath = getRealPathFromURI(selectedImageUri);
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                editprofile_picture.setImageBitmap(bm);

            }
        }
    }

    private void takePhotoAction(){
        //get FilePath
        File f = new File(Environment.getExternalStorageDirectory()
                .toString());
        //file temp image for use & deleting after take a photo
        for (File temp : f.listFiles()) {
            if (temp.getName().equals(CAMERA_IMG_NAME)) {
                f = temp;
                break;
            }
        }
        try {
            uploadfile(Uri.fromFile(f));
            Bitmap bm;
            BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

            bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
                    btmapOptions);

            // bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
            editprofile_picture.setImageBitmap(bm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadfile(Uri imageUri){
        Log.i(TAG, "StartUploading File");
        storageReference.putFile(imageUri).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(EditProfileActivity.this, "Upload success", Toast.LENGTH_SHORT);
                        Log.i(TAG, "Upload success");
                        Log.i(TAG, taskSnapshot.getDownloadUrl().toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Please Try again", Toast.LENGTH_SHORT);
                        Log.i(TAG, "Upload fail");
                    }
                });
    }


    //TODO understand this
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
