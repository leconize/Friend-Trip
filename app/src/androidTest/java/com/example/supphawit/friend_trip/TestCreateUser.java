package com.example.supphawit.friend_trip;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import com.example.supphawit.friend_trip.model.User;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(AndroidJUnit4.class)
public class TestCreateUser {
    private static final String TAG = "TestCreateUser";
    private User testUser, testParcel;

    @Test
    public void testCreateUser(){
        testUser = createProfile(new User());

        Parcel parcel = Parcel.obtain();
        testUser.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        testParcel = User.CREATOR.createFromParcel(parcel);


        Assert.assertEquals("signin@test.project", testUser.getEmail());
        Assert.assertEquals("-KUF00w71yv-s7tDIIO5", testUser.getFirebaseid());
        Assert.assertEquals("signin_test", testUser.getNickname());
        Assert.assertEquals(testUser.getEmail(), testParcel.getEmail());
    }

    private User createProfile(User user){
        user.setEmail("signin@test.project");
        user.setFirebaseid("-KUF00w71yv-s7tDIIO5");
        user.setNickname("signin_test");

        return user;
    }

}
