package com.example.supphawit.friend_trip.trip.model;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Supphawit on 4/10/2559.
 */

public class Trip implements Serializable{

    private String name;
    private String creatername;
    private String createrid;
    private ArrayList<String> joinerId_list;

    public ArrayList<String> getJoinerId_list() {
        return joinerId_list;
    }

    public void setJoinerId_list(ArrayList<String> joinerId_list) {
        this.joinerId_list = joinerId_list;
    }

    private ArrayList<String> tags;

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    private int maxpeople;
    private String starttime;
    private String endtime;
    private String startdate;
    private String enddate;
    private String description;
    private String id;
    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private double longitude;


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
