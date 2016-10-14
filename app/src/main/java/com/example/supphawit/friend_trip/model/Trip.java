package com.example.supphawit.friend_trip.model;


/**
 * Created by Supphawit on 4/10/2559.
 */

public class Trip {

    private String tripname;
    private String creatername;
    private String places;
    private int maxperson;
    private String starttime;
    private String endtime;
    private String startdate;
    private String enddate;
    private String description;

    public String getTripname() {
        return tripname;
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

    public void setTripname(String name) {
        this.tripname = name;
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

    public int getMaxperson() {
        return maxperson;
    }

    public void setMaxperson(int maxperson) {
        this.maxperson = maxperson;
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
