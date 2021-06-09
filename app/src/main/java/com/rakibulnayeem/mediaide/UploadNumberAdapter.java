package com.rakibulnayeem.mediaide;

public class UploadNumberAdapter {
    String uid,phoneNumber;

    public UploadNumberAdapter() {
    }

    public UploadNumberAdapter(String uid, String phoneNumber) {
        this.uid = uid;
        this.phoneNumber = phoneNumber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
