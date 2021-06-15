package com.rakibulnayeem.mediaide.Posts;

public class PostAdapter {

    private String uid, key,current_time, hospital_name,blood_group,date_time,details,zilla,phone_number;

    public PostAdapter() {
    }

    public PostAdapter(String uid, String key, String current_time, String hospital_name, String blood_group, String date_time, String details, String zilla, String phone_number) {
        this.uid = uid;
        this.key = key;
        this.current_time = current_time;
        this.hospital_name = hospital_name;
        this.blood_group = blood_group;
        this.date_time = date_time;
        this.details = details;
        this.zilla = zilla;
        this.phone_number = phone_number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getZilla() {
        return zilla;
    }

    public void setZilla(String zilla) {
        this.zilla = zilla;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
