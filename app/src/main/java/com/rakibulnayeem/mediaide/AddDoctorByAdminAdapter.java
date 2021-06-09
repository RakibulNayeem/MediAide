package com.rakibulnayeem.mediaide;

public class AddDoctorByAdminAdapter {

    private String uid,key,name,hospital_name,hospital_id,degree,speciality,fee,chamber_address,zilla,phone_number,active_day,open_time,ampm,close_time,ampm2,imageUri;

    public AddDoctorByAdminAdapter() {
    }


    public AddDoctorByAdminAdapter(String uid, String key, String name, String hospital_name, String hospital_id, String degree, String speciality, String fee, String chamber_address, String zilla, String phone_number, String active_day, String open_time, String ampm, String close_time, String ampm2, String imageUri) {
        this.uid = uid;
        this.key = key;
        this.name = name;
        this.hospital_name = hospital_name;
        this.hospital_id = hospital_id;
        this.degree = degree;
        this.speciality = speciality;
        this.fee = fee;
        this.chamber_address = chamber_address;
        this.zilla = zilla;
        this.phone_number = phone_number;
        this.active_day = active_day;
        this.open_time = open_time;
        this.ampm = ampm;
        this.close_time = close_time;
        this.ampm2 = ampm2;
        this.imageUri = imageUri;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getChamber_address() {
        return chamber_address;
    }

    public void setChamber_address(String chamber_address) {
        this.chamber_address = chamber_address;
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

    public String getActive_day() {
        return active_day;
    }

    public void setActive_day(String active_day) {
        this.active_day = active_day;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getAmpm() {
        return ampm;
    }

    public void setAmpm(String ampm) {
        this.ampm = ampm;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getAmpm2() {
        return ampm2;
    }

    public void setAmpm2(String ampm2) {
        this.ampm2 = ampm2;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
