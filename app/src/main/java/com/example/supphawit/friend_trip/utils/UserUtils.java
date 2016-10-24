package com.example.supphawit.friend_trip.utils;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Supphawit on 10/24/2016.
 */

public class UserUtils {

    public static String getUserId(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static boolean isUserInAuth(){
        return ! (FirebaseAuth.getInstance().getCurrentUser() == null);
    }
}
