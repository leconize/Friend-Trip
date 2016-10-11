package com.example.supphawit.friend_trip.listener;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Supphawit on 11/10/2559.
 */

public class DatabaseValueListener implements DatabaseReference.CompletionListener {

    public static final String TAG = "DATABASE ERROR";
    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if(databaseError != null){
            Log.d(TAG, databaseError.getMessage() + "At" + databaseReference.toString());
        }
    }
}
