package com.rakibulnayeem.mediaide;

public class AddHealthCareAdapter {

    private String key,title,sub_title,description;

    public AddHealthCareAdapter() {
    }


    public AddHealthCareAdapter(String key, String title, String sub_title, String description) {
        this.key = key;
        this.title = title;
        this.sub_title = sub_title;
        this.description = description;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
