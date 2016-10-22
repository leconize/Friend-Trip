package com.example.supphawit.friend_trip.model;


import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Supphawit on 4/10/2559.
 */

public class Trip {

    private String name;
    private String creatername;
    private String createrid;




    private List<String> places;
    private int maxpeople;
    private String starttime;
    private String endtime;
    private String startdate;
    private String enddate;
    private String description;
    private String id;



    public String getName() {
        return name;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatername() {
        return creatername;
    }

    public void setCreatername(String creatername) {
        this.creatername = creatername;
    }

    public String getPlaces() {
        return Joiner.on(" ").join(places);
    }

    public void setPlaces(List<String> places) {
        this.places = places;
    }

    public int getMaxpeople() {
        return maxpeople;
    }


    public void setMaxpeople(String maxpeople) {
        this.maxpeople = Integer.parseInt(maxpeople);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Trip() {

    }

    public String getCreaterid() {
        return createrid;
    }

    public void setCreaterid(String createrid) {
        this.createrid = createrid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Trip && (((Trip) obj).id) .equals(this.id);
    }
}
