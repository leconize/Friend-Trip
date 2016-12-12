package com.example.supphawit.friend_trip.utils;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.supphawit.friend_trip.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Supphawit on 12/13/2016.
 * g.supavit@gmail.com
 */

public class MyUtils {

    private static final String TAG = "MyUtils";

    public static void setNotificationValue(final Menu menu){
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("invite/"+ UserUtils.getUserId());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                View count = menu.findItem(R.id.mail_noti).getActionView();
                                ((TextView)count.findViewById(R.id.hotlist_hot)).setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                }

                                @Override
                        public void onCancelled(DatabaseError databaseError) {
                               Log.d(TAG, databaseError.getMessage());
                            }

                           });

                    }
}
