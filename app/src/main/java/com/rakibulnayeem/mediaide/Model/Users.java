package com.rakibulnayeem.mediaide.Model;

public class Users {
    String adder_uid, blood_group, donate_switch, email, last_donation_date, name, phone_number, status, uid, upazila, village, zilla;

    public Users(String adder_uid, String blood_group, String donate_switch, String email, String last_donation_date, String name, String phone_number, String status, String uid, String upazila, String village, String zilla) {
        this.adder_uid = adder_uid;
        this.blood_group = blood_group;
        this.donate_switch = donate_switch;
        this.email = email;
        this.last_donation_date = last_donation_date;
        this.name = name;
        this.phone_number = phone_number;
        this.status = status;
        this.uid = uid;
        this.upazila = upazila;
        this.village = village;
        this.zilla = zilla;
    }

    public String getAdder_uid() {
        return adder_uid;
    }

    public void setAdder_uid(String adder_uid) {
        this.adder_uid = adder_uid;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getDonate_switch() {
        return donate_switch;
    }

    public void setDonate_switch(String donate_switch) {
        this.donate_switch = donate_switch;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLast_donation_date() {
        return last_donation_date;
    }

    public void setLast_donation_date(String last_donation_date) {
        this.last_donation_date = last_donation_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUpazila() {
        return upazila;
    }

    public void setUpazila(String upazila) {
        this.upazila = upazila;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getZilla() {
        return zilla;
    }

    public void setZilla(String zilla) {
        this.zilla = zilla;
    }
}
