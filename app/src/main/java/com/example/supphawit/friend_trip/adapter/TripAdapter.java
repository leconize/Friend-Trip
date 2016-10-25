package com.example.supphawit.friend_trip.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.model.Trip;
import com.example.supphawit.friend_trip.utils.StorageUtils;
import com.example.supphawit.friend_trip.utils.UserUtils;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Supphawit on 14/10/2559.
 */

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder>{

    private List<Trip> trips;
    private Context context;

    public TripAdapter(List<Trip> trips, Context context) {
        this.trips = trips;
        this.context = context;
    }

    private Context getContext(){
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contractView = inflater.inflate(R.layout.triprow_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(contractView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trip trip = trips.get(position);
        TextView tripname = holder.tripname;
        tripname.setText(trip.getName());
        TextView tripdate = holder.tripdate;
        tripdate.setText(trip.getStartdate());
        TextView tripplaces = holder.tripplaces;
        tripplaces.setText(trip.getPlaceString());
        ImageView trippicture = holder.trippicture;
        TextView owner_name = holder.trip_creator_name;
        owner_name.setText(trip.getCreatername());
        ImageView profile_pic = holder.userprofile;
        Glide.with(context)
                .load("").fitCenter()
                .placeholder(R.drawable.pic)
                .into(trippicture);
        StorageUtils.loadProfilePicture(context, profile_pic, UserUtils.getUserId());
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.list_tripname) public TextView tripname;
        @BindView(R.id.trip_startdate) public TextView tripdate;
        @BindView(R.id.list_trip_place) public TextView tripplaces;
        @BindView(R.id.trip_img) public ImageView trippicture;
        @BindView(R.id.list_profile_image) public CircleImageView userprofile;
        @BindView(R.id.list_username) public TextView trip_creator_name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }


}
