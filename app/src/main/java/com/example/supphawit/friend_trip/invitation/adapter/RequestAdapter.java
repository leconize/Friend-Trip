package com.example.supphawit.friend_trip.invitation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.supphawit.friend_trip.invitation.RequestModel;
import com.example.supphawit.friend_trip.user.model.User;

import java.util.List;

/**
 * Created by Supphawit on 12/5/2016.
 * g.supavit@gmail.com
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<RequestModel> requestModels;
    private Context context;
    private static String TAG = "RequestAdapter";

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
