package com.rakibulnayeem.mediaide;

public class SwitchAdapter {
    private String uid,switch_value;

    public SwitchAdapter() {
    }

    public SwitchAdapter(String uid, String switch_value) {
        this.uid = uid;
        this.switch_value = switch_value;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSwitch_value() {
        return switch_value;
    }

    public void setSwitch_value(String switch_value) {
        this.switch_value = switch_value;
    }
}
