package com.example.supphawit.friend_trip.model;

import android.media.TimedText;
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Supphawit on 4/10/2559.
 */

public class Trip {

    private String name;
    private String creatername;
    private String places;
    private int maxpeople;
    private String starttime;
    private String endtime;
    private String startdate;
    private String enddate;
    private String description;

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
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public int getMaxpeople() {
        return maxpeople;
    }

    public void setMaxpeople(int maxpeople) {
        this.maxpeople = maxpeople;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Trip() {

    }
}
