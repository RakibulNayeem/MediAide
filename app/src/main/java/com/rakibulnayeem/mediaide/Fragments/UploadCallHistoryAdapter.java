package com.rakibulnayeem.mediaide.Fragments;

public class UploadCallHistoryAdapter {

    private  String key, current_uid, name, type,phone_number, current_time;

    public UploadCallHistoryAdapter() {
    }


    public UploadCallHistoryAdapter(String key, String current_uid, String name, String type, String phone_number, String current_time) {
        this.key = key;
        this.current_uid = current_uid;
        this.name = name;
        this.type = type;
        this.phone_number = phone_number;
        this.current_time = current_time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCurrent_uid() {
        return current_uid;
    }

    public void setCurrent_uid(String current_uid) {
        this.current_uid = current_uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }
}
