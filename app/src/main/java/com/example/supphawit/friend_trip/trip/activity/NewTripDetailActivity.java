package com.example.supphawit.friend_trip.trip.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.invitation.activity.ChooseInviActivity;
import com.example.supphawit.friend_trip.invitation.activity.RequestListActivity;
import com.example.supphawit.friend_trip.trip.model.Trip;
import com.example.supphawit.friend_trip.user.activity.SignInActivity;
import com.example.supphawit.friend_trip.user.activity.ViewProfileActivity;
import com.example.supphawit.friend_trip.utils.DatabaseUtils;
import com.example.supphawit.friend_trip.utils.MyUtils;
import com.example.supphawit.friend_trip.utils.StorageUtils;
import com.example.supphawit.friend_trip.utils.UserUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewTripDetailActivity extends AppCompatActivity {

    private final String CAMERA_IMG_NAME = "photo.jpg";
    public final String APP_TAG = "FRIEND_TRIP";
    private final int REQUEST_CAMERA = 269;
    private final int REQUEST_SELECT_FILE = 369;
    @BindView(R.id.image) ImageView img;
    private static String TAG = "TripDetailActivity";
    private Trip trip;
    @BindView(R.id.trip_detail_ownername) TextView ownername;
    @BindView(R.id.trip_detail_peoplenum) TextView peoplenum;
    @BindView(R.id.trip_detail_starttime) TextView starttime;
    @BindView(R.id.trip_detail_endtime) TextView endtime;
    @BindView(R.id.devpagetoolbar) Toolbar toolbar;
    @BindView(R.id.new_join_btn) Button join_btn;
    @BindView(R.id.new_back_btn) Button back_btn;
    @BindView(R.id.detail_profilepic) CircleImageView profile_pic;
    @BindView(R.id.trip_description) TextView description;


    private ArrayList<String> memberIdList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_trip_detail);
        ButterKnife.bind(this);
        trip = (Trip) getIntent().getSerializableExtra("trip");
        setupText(trip);
        if(UserUtils.getUserId().equals(trip.getCreaterid())){
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectImage();
                }
            });
        }
        setSupportActionBar(toolbar);
    }

    private void setupText(Trip trip){
        memberIdList = trip.getJoinerId_list();
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(trip.getName());
        ownername.setText(trip.getCreatername());
        int currentjoiner = memberIdList.size();
        peoplenum.setText(currentjoiner + "/" + trip.getMaxpeople());
        starttime.setText(trip.getStartdate() + " " + trip.getStarttime());
        endtime.setText(trip.getEnddate() + " " + trip.getEndtime());
        description.setText(trip.getDescription());
        StorageUtils.loadProfilePicture(this, profile_pic, trip.getCreaterid());
        StorageUtils.loadTripbg(this, img, trip.getId());
        setUpButton();

    }

    private void setUpButton(){
        if(memberIdList.contains(UserUtils.getUserId())){
            if(trip.getCreaterid().equals(UserUtils.getUserId())){
                join_btn.setText("Invite user");
            }
            else{
                back_btn.setVisibility(View.INVISIBLE);
                join_btn.setText("Leave this Trip");
            }
        }
        else if(memberIdList.size() >= trip.getMaxpeople()){
            back_btn.setVisibility(View.INVISIBLE);
            join_btn.setText("Trip is full");
            join_btn.setEnabled(false);
        }
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
            case R.id.mail_noti:
                checkNoti();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void checkNoti(){
        Intent intent = new Intent(this, RequestListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.new_join_btn)
    public void joinBtnClick(){
        DatabaseReference tripMemberRef = DatabaseUtils.getTripRef(trip.getId()).child("joinerId_list");
        final ArrayList<String> memberIdList = trip.getJoinerId_list();
        if(trip.getCreaterid().equals(UserUtils.getUserId())){
            Log.i(TAG, "go to invite page");
            Intent intent = new Intent(this, ChooseInviActivity.class);
            intent.putExtra("trip", trip);
            startActivity(intent);
            return;
        }
        else if(memberIdList.contains(UserUtils.getUserId())){
            Log.i(TAG, "user leave trip");
            memberIdList.remove(UserUtils.getUserId());
        }
        else{
            Log.i(TAG, "user join trip");
            memberIdList.add(UserUtils.getUserId());
        }
        tripMemberRef.setValue(memberIdList);
        finish();
        startActivity(getIntent());
    }

    @OnClick(R.id.new_back_btn)
    public void exitBtnClick(){
        finish();
        Intent intent = new Intent(this, TripListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ownerbtn)
    public void seeOwnerDetail(){
        Intent intent = new Intent(this, ViewProfileActivity.class);
        intent.putExtra("user_id", trip.getCreaterid());
        startActivity(intent);
    }

    @OnClick(R.id.joinerbtn)
    public void seeJoinerList(){
        Intent intent = new Intent(this, JoinersListActivity.class);
        intent.putExtra("list", trip.getJoinerId_list());
        startActivity(intent);
    }

    @OnClick(R.id.meetingbtn)
    public void seeMeetingPoint(){
        Intent intent = new Intent(this, MapsActivity.class);
        Log.i(TAG, "lat = " + trip.getLatitude());
        intent.putExtra("lat", trip.getLatitude());
        intent.putExtra("lng", trip.getLongitude());
        startActivity(intent);
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
            } else if (requestCode == REQUEST_SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                Glide.with(NewTripDetailActivity.this).load(selectedImageUri).skipMemoryCache(true).diskCacheStrategy( DiskCacheStrategy.NONE )
                        .into(img);
                uploadfile(selectedImageUri);
            }
        }
    }

    private void takePhotoAction(){
        Uri takenPhotoUri = getPhotoFileUri(CAMERA_IMG_NAME);
        Glide.with(NewTripDetailActivity.this).load(takenPhotoUri).skipMemoryCache(true).diskCacheStrategy( DiskCacheStrategy.NONE )
                .into(img);
        uploadfile(takenPhotoUri);
    }

    private void uploadfile(final Uri imageUri){
        Log.i(TAG, "StartUploading File");
        StorageUtils.getTripbgRef(trip.getId()).putFile(imageUri).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i(TAG, "Upload success");
                        Log.i(TAG, taskSnapshot.getDownloadUrl().toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewTripDetailActivity.this, "Please Try again", Toast.LENGTH_SHORT);
                        Log.i(TAG, "Upload fail");
                    }
                });
    }
}
