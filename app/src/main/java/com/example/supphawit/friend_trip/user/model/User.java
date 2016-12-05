package com.example.supphawit.friend_trip.user.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String firstname, lastname, nickname, gender;
    private String facebook, birthday, mobile, address, email;
    private String firebaseid, pictureurl;

    public User() {}

    //Signup Constructor
    public User(String email, String nickname){
        this.email = email;
        this.nickname = nickname;
    }

    //Login Constructor
    public User (String email, String firebaseid, String firstname, String lastname, String nickname,
                 String gender, String facebook, String birthday, String mobile,
                 String address){
        this.firstname = firstname;
        this.lastname = lastname;
        this.nickname = nickname;
        this.gender = gender;
        this.facebook = facebook;
        this.birthday = birthday;
        this.mobile = mobile;
        this.address = address;
        this.email = email;
        this.firebaseid = firebaseid;
    }

    //Parcel Constructor
    public User (Parcel in){
        String[] data = new String[11];

        in.readStringArray(data);
        this.email = data[0];
        this.firebaseid = data[1];
        this.firstname = data[2];
        this.lastname = data[3];
        this.nickname = data[4];
        this.gender = data[5];
        this.facebook = data[6];
        this.birthday = data[7];
        this.mobile = data[8];
        this.address = data[9];
        this.pictureurl = data[10];
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.email, this.firebaseid, this.firstname, this.lastname, this.nickname,
                this.gender, this.facebook, this.birthday, this.mobile, this.address, this.pictureurl
        });
    }

    //Parcel Creator
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) { return new User(in); }
        public User[] newArray(int size) { return new User[size]; }
    };

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getFacebook() { return facebook; }
    public void setFacebook(String facebook) { this.facebook = facebook; }
    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirebaseid() { return firebaseid; }
    public void setFirebaseid(String firebaseid) { this.firebaseid = firebaseid; }

    public String getPictureurl() {
        return pictureurl;
    }

    public void setPictureurl(String pictureurl) {
        this.pictureurl = pictureurl;
    }
}
