package com.example.supphawit.friend_trip.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.model.Trip;

import org.w3c.dom.Text;

import java.util.List;


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
        tripname.setText(trip.getTripname());
        TextView tripdate = holder.tripdate;
        tripname.setText(trip.getStartdate());
        TextView tripplaces = holder.tripplaces;
        tripname.setText(trip.getPlaces());
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tripname;
        public TextView tripdate;
        public TextView tripplaces;
        public ViewHolder(View itemView) {
            super(itemView);
            tripname = (TextView) itemView.findViewById(R.id.trip_name);
            tripdate = (TextView) itemView.findViewById(R.id.trip_date);
            tripplaces = (TextView) itemView.findViewById(R.id.trip_places);
        }
    }
}
