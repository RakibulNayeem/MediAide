package com.rakibulnayeem.mediaide;

public class AddHospitalAdapter {

    private String key,name,category,address,zilla,phone_number;


    public AddHospitalAdapter() {

    }

    public AddHospitalAdapter(String key, String name, String category, String address, String zilla, String phone_number) {
        this.key = key;
        this.name = name;
        this.category = category;
        this.address = address;
        this.zilla = zilla;
        this.phone_number = phone_number;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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