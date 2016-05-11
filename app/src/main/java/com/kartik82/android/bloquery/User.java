package com.kartik82.android.bloquery;

/**
 * Created by Kartik on 26-Apr-16.
 */
public class User {

    private String display_name;
    private String provider;
    private String photo_url;
    private String full_name;
    private String description;

    public User() {
    }

    public User(String display_name, String provider, String photo_url, String full_name, String description) {
        this.display_name = display_name;
        this.provider = provider;
        this.photo_url = photo_url;
        this.full_name = full_name;
        this.description = description;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
