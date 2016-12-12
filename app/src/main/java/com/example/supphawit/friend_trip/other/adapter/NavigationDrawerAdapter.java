package com.example.supphawit.friend_trip.other.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.trip.activity.TripListActivity;
import com.example.supphawit.friend_trip.user.activity.SignInActivity;
import com.example.supphawit.friend_trip.user.activity.ViewProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import butterknife.BindView;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder> {

    private static String TAG = "NavigationDrawerAdapter";

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[];
    private int mIcons[];

    private String name;
    private String email;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;

        TextView textView;
        ImageView imageView;
        TextView Name;
        TextView Email;
        //RecyclerView drawer;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.textDrawer);
                imageView = (ImageView) itemView.findViewById(R.id.iconDrawer);
                Holderid = 1;
            } else {
                Name = (TextView) itemView.findViewById(R.id.header_name);
                Email = (TextView) itemView.findViewById(R.id.header_email);
                Holderid = 0;
            }
        }
    }

    public NavigationDrawerAdapter(String titles[], int icons[], String name, String email) {
        this.mNavTitles = titles;
        this.mIcons = icons;
        this.name = name;
        this.email = email;
    }

    @Override
    public NavigationDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row, parent, false);
            ViewHolder vhItem = new ViewHolder(v, viewType);
            return vhItem;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);
            ViewHolder vhHeader = new ViewHolder(v, viewType);
            return vhHeader;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(NavigationDrawerAdapter.ViewHolder holder, final int position) {
        if (holder.Holderid == 1) {
            holder.textView.setText(mNavTitles[position - 1]);
            holder.imageView.setImageResource(mIcons[position - 1]);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectItem(position, view.getContext());
                }
            });
        } else {
            holder.Name.setText(name);
            holder.Email.setText(email);
        }
    }

    @Override
    public int getItemCount() {
        return mNavTitles.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private void selectItem(int position, Context context) {
        Log.d(TAG, position + "");
        switch (position) {
            case 1:
                if(!context.getClass().getName().equals(TripListActivity.class.getName()))
                    context.startActivity(new Intent(context, TripListActivity.class));
                return;
            case 2:
                if(!context.getClass().getName().equals(ViewProfileActivity.class.getName()))
                    context.startActivity(new Intent(context, ViewProfileActivity.class));
                return;
            case 3:
                FirebaseAuth.getInstance().signOut();
                context.startActivity(new Intent(context, SignInActivity.class));
                ((Activity) context).finish();
                return;
        }
    }
}

