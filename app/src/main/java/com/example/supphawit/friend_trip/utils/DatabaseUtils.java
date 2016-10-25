package com.example.supphawit.friend_trip.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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




}
