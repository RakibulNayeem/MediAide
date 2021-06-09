package com.rakibulnayeem.mediaide;

public class AddAmbulanceAdapter {

    private String uid, key,name, driver_name, address,zilla,vehicle_no, service_type, available_switch_value, phone_number;

    public AddAmbulanceAdapter() {
    }


    public AddAmbulanceAdapter(String uid, String key, String name, String driver_name, String address, String zilla, String vehicle_no, String service_type, String available_switch_value, String phone_number) {
        this.uid = uid;
        this.key = key;
        this.name = name;
        this.driver_name = driver_name;
        this.address = address;
        this.zilla = zilla;
        this.vehicle_no = vehicle_no;
        this.service_type = service_type;
        this.available_switch_value = available_switch_value;
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

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
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

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getAvailable_switch_value() {
        return available_switch_value;
    }

    public void setAvailable_switch_value(String available_switch_value) {
        this.available_switch_value = available_switch_value;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
