package com.rakibulnayeem.mediaide.BloodBanks;

public class AddBBankAdapter {
    private String uid,key, name_bb,address_bb, open_bb, zilla_bb,phone_number_bb;


    public AddBBankAdapter() {

    }

    public AddBBankAdapter(String uid, String key, String name_bb, String address_bb, String open_bb, String zilla_bb, String phone_number_bb) {
        this.uid = uid;
        this.key = key;
        this.name_bb = name_bb;
        this.address_bb = address_bb;
        this.open_bb = open_bb;
        this.zilla_bb = zilla_bb;
        this.phone_number_bb = phone_number_bb;
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

    public String getName_bb() {
        return name_bb;
    }

    public void setName_bb(String name_bb) {
        this.name_bb = name_bb;
    }

    public String getAddress_bb() {
        return address_bb;
    }

    public void setAddress_bb(String address_bb) {
        this.address_bb = address_bb;
    }

    public String getOpen_bb() {
        return open_bb;
    }

    public void setOpen_bb(String open_bb) {
        this.open_bb = open_bb;
    }

    public String getZilla_bb() {
        return zilla_bb;
    }

    public void setZilla_bb(String zilla_bb) {
        this.zilla_bb = zilla_bb;
    }

    public String getPhone_number_bb() {
        return phone_number_bb;
    }

    public void setPhone_number_bb(String phone_number_bb) {
        this.phone_number_bb = phone_number_bb;
    }
}
