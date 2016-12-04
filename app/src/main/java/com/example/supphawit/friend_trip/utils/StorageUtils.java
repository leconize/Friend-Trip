package com.example.supphawit.friend_trip.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.supphawit.friend_trip.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Supphawit on 10/24/2016.
 */

public class StorageUtils {

    private static String TAG = "StorageUtils";


    public static void loadProfilePicture(final Context context, final ImageView profile_img, final String userid) {
        Log.i(TAG, "Load Picture");
        getProfileStorageRef(userid)
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    if(context != null){
                        Log.i(TAG, profile_img.toString());
                        Glide.with(context).load(uri).placeholder(R.drawable.ic_user_profile).fitCenter().dontAnimate().into(profile_img);
                    }
                } catch (Exception e) {
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


    public static StorageReference getProfileStorageRef(String userid) {
        return FirebaseStorage.getInstance().getReference("profile_pic/" + userid + ".jpg");
    }


}
