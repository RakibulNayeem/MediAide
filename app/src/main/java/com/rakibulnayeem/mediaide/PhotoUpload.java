package com.rakibulnayeem.mediaide;

public class PhotoUpload {

    private String uid;
    private String key;
    private String imageUri;


    public PhotoUpload()
    {

    }

    public PhotoUpload(String uid, String key, String imageUri) {
        this.uid = uid;
        this.key = key;
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
