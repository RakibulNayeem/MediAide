package com.rakibulnayeem.mediaide;

public class PPUpload {

    private String email;
    private String imageUri;

    public PPUpload()
    {

    }

    public PPUpload(String email, String imageUri) {
        this.email = email;
        this.imageUri = imageUri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
