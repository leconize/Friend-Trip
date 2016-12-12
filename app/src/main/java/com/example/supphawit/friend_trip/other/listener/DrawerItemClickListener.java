package com.example.supphawit.friend_trip.other.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.supphawit.friend_trip.trip.activity.TripListActivity;

public class DrawerItemClickListener implements ListView.OnItemClickListener {

    private static String TAG = "DrawerItemClickListener";

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectItem(i, view.getContext());
    }

    private void selectItem(int position, Context context) {
        Log.d(TAG, position+"");
        switch (position){
            case 0:
                Intent intent = new Intent(context, TripListActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
        }
    }
}
