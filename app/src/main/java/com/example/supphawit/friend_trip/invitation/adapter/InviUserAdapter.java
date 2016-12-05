package com.example.supphawit.friend_trip.invitation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.supphawit.friend_trip.R;
import com.example.supphawit.friend_trip.invitation.RequestModel;
import com.example.supphawit.friend_trip.trip.model.Trip;
import com.example.supphawit.friend_trip.user.model.User;
import com.example.supphawit.friend_trip.utils.StorageUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Supphawit on 12/5/2016.
 * g.supavit@gmail.com
 */

public class InviUserAdapter extends RecyclerView.Adapter<InviUserAdapter.ViewHolder> {

    private List<User> users;
    private Context context;
    private static String TAG = "JoinerAdapter";
    private Trip trip;

    public InviUserAdapter(Context context, List<User> joiners, Trip trip_id) {
        this.users = joiners;
        this.context = context;
        this.trip = trip_id;
    }

    private Context getContext(){
        return context;
    }

    @Override
    public InviUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contractView = inflater.inflate(R.layout.joinerrow_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(contractView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);
        holder.name.setText(user.getFirstname() + " " + user.getLastname());
        holder.gender.setText(user.getGender());
        StorageUtils.loadProfilePicture(getContext(), holder.profile_pic, user.getFirebaseid());
        Log.i(TAG, "load viewholder");
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.joiner_profilepic)
        ImageView profile_pic;
        @BindView(R.id.joiner_name)
        TextView name;
        @BindView(R.id.joiner_gender) TextView gender;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                final User user = users.get(position);
                MaterialDialog dialog = new MaterialDialog.Builder(context)
                        .title("Confirm Invitation")
                        .content("You want to Invite this person to your trip")
                        .positiveText("Yes")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                final DatabaseReference database = FirebaseDatabase.getInstance().getReference("invite/"+user.getFirebaseid());
                                database.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        ArrayList<RequestModel> trips = new ArrayList<>();
                                        try{
                                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                                RequestModel name = dataSnapshot1.getValue(RequestModel.class);
                                                trips.add(name);
                                            }
                                            Log.d(TAG, trips.toString());
                                            RequestModel model = new RequestModel();
                                            model.setTrip_id(trip.getId());
                                            model.setCreator_name(trip.getCreatername());
                                            trips.add(model);
                                            database.setValue(trips);
                                        }
                                        catch (Exception e){
                                            RequestModel model = new RequestModel();
                                            model.setTrip_id(trip.getId());
                                            model.setCreator_name(trip.getCreatername());
                                            trips.add(model);
                                            database.setValue(trips);
                                            Log.d(TAG, "create new invite");
                                        }
                                        users.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, users.size());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        ArrayList<RequestModel> trips = new ArrayList<RequestModel>();
                                        RequestModel model = new RequestModel();
                                        model.setTrip_id(trip.getId());
                                        model.setCreator_name(trip.getCreatername());
                                        trips.add(model);
                                        database.setValue(trips);
                                        Log.d(TAG, "create new invite");
                                        users.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, users.size());
                                    }
                                });
                            }
                        })
                        .negativeText("No")
                        .show();
            }
        }
    }
}
