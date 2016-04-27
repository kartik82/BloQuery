package com.kartik82.android.bloquery;

/**
 * Created by Kartik on 26-Apr-16.
 */
public class User {

    private String display_name;
    private String provider;

    public User() {
    }

    public User(String display_name, String provider) {
        this.display_name = display_name;
        this.provider = provider;
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
}
