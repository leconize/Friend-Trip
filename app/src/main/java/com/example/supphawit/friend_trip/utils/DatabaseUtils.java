package com.example.supphawit.friend_trip.utils;

import android.util.Log;

import com.example.supphawit.friend_trip.user.activity.ViewProfileActivity;
import com.example.supphawit.friend_trip.user.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by Supphawit on 10/24/2016.
 */

public class DatabaseUtils {

    public static DatabaseReference getUsersRef(){
        return FirebaseDatabase.getInstance().getReference("/users");
    }

    public static DatabaseReference getUserProfileRef(){
        return FirebaseDatabase.getInstance().getReference("/profile_pic");
    }

    public static DatabaseReference getTripRef(){
        return FirebaseDatabase.getInstance().getReference("/trips");
    }

    public static DatabaseReference getTripRef(String trip_id){
        return FirebaseDatabase.getInstance().getReference("/trips/"+trip_id);
    }


    public static DatabaseReference getDbRef(){
        return FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getTripMemberRef(String trip_id){
        return FirebaseDatabase.getInstance().getReference("trips/"+trip_id+"/joinerId_list");
    }


}
