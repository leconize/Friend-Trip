package com.example.supphawit.friend_trip.invitation;

import java.io.Serializable;

/**
 * Created by Supphawit on 12/5/2016.
 * g.supavit@gmail.com
 */

public class RequestModel implements Serializable{

    String trip_id;
    String creator_name;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }
}
