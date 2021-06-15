package com.rakibulnayeem.mediaide.SignUpLogIn;

public class SignUpAdapter {

    private String name,uid,adder_uid,email,village,blood_group,upazila,zilla,phone_number,last_donation_date,status,donate_switch;

    public SignUpAdapter() {
    }

    public SignUpAdapter(String name, String uid, String adder_uid, String email, String village, String blood_group, String upazila, String zilla, String phone_number, String last_donation_date, String status, String donate_switch) {
        this.name = name;
        this.uid = uid;
        this.adder_uid = adder_uid;
        this.email = email;
        this.village = village;
        this.blood_group = blood_group;
        this.upazila = upazila;
        this.zilla = zilla;
        this.phone_number = phone_number;
        this.last_donation_date = last_donation_date;
        this.status = status;
        this.donate_switch = donate_switch;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAdder_uid() {
        return adder_uid;
    }

    public void setAdder_uid(String adder_uid) {
        this.adder_uid = adder_uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getUpazila() {
        return upazila;
    }

    public void setUpazila(String upazila) {
        this.upazila = upazila;
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

    public String getLast_donation_date() {
        return last_donation_date;
    }

    public void setLast_donation_date(String last_donation_date) {
        this.last_donation_date = last_donation_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDonate_switch() {
        return donate_switch;
    }

    public void setDonate_switch(String donate_switch) {
        this.donate_switch = donate_switch;
    }
}
