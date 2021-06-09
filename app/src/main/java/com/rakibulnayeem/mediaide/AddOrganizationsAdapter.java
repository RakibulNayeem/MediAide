package com.rakibulnayeem.mediaide;

public class AddOrganizationsAdapter {


    private String uid, key, name, address, open, zilla, phone_number;

    public AddOrganizationsAdapter() {

    }

    public AddOrganizationsAdapter(String uid, String key, String name, String address, String open, String zilla, String phone_number) {
        this.uid = uid;
        this.key = key;
        this.name = name;
        this.address = address;
        this.open = open;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
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
