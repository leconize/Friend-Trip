package com.example.supphawit.friend_trip.trip.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.trip.activity.NewTripDetailActivity;
import com.example.supphawit.friend_trip.trip.model.Trip;
import com.example.supphawit.friend_trip.user.activity.ViewProfileActivity;
import com.example.supphawit.friend_trip.user.model.User;
import com.example.supphawit.friend_trip.utils.StorageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Supphawit on 12/4/2016.
 * g.supavit@gmail.com
 */

public class JoinerAdapter extends RecyclerView.Adapter<JoinerAdapter.ViewHolder> {

    private List<User> joiners;
    private Context context;
    private static String TAG = "JoinerAdapter";

    public JoinerAdapter(Context context, List<User> joiners) {
        this.joiners = joiners;
        this.context = context;
    }

    private Context getContext(){
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contractView = inflater.inflate(R.layout.joinerrow_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(contractView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = joiners.get(position);
        holder.name.setText(user.getFirstname() + " " + user.getLastname());
        holder.gender.setText(user.getGender());
        StorageUtils.loadProfilePicture(getContext(), holder.profile_pic, user.getFirebaseid());
        Log.i(TAG, "load viewholder");
    }

    @Override
    public int getItemCount() {
        return joiners.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.joiner_profilepic) ImageView profile_pic;
        @BindView(R.id.joiner_name) TextView name;
        @BindView(R.id.joiner_gender) TextView gender;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                User joiner = joiners.get(position);
                Intent intent = new Intent(context, ViewProfileActivity.class);
                intent.putExtra("user_id", joiner.getFirebaseid());
                context.startActivity(intent);
            }
        }
    }
}
