package com.example.supphawit.friend_trip.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.activity.EditProfileActivity;
import com.example.supphawit.friend_trip.activity.ViewProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

/**
 * Created by Supphawit on 10/24/2016.
 */

public class StorageUtils {

    private static String TAG = "StorageUtils";


    public static void loadProfilePicture(final Context context,final ImageView profile_img,final String userid) {
        Log.i(TAG, "Load Picture");
        DatabaseUtils.getUserProfileRef().child(userid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i(TAG, dataSnapshot.toString());
                        if (dataSnapshot.exists()) {
                            Log.i(TAG, "start downloading");
                            getProfileStorageRef(userid)
                                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    try {
                                        Glide.with(context).load(uri).placeholder(R.drawable.profile_picture).into(profile_img);
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
                        Log.d(TAG, databaseError.getMessage());
                    }
                });
    }

    public static StorageReference getProfileStorageRef(String userid){
        return FirebaseStorage.getInstance().getReference("profile_pic/" + userid +".jpg");
    }


}
