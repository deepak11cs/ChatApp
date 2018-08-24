package com.example.dev.kmv_mobile.models;

public class User {
    private String name;
    private String device_token;
    private String email;
    private String uid;
    private String profilepic_lg;
    private String profilepic_sm;
    private String status;
    private String store_points;

    public User() {
    }

    public User(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfilepic_lg() {
        return profilepic_lg;
    }

    public void setProfilepic_lg(String profilepic_lg) {
        this.profilepic_lg = profilepic_lg;
    }

    public String getProfilepic_sm() {
        return profilepic_sm;
    }

    public void setProfilepic_sm(String profilepic_sm) {
        this.profilepic_sm = profilepic_sm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStore_points() {
        return store_points;
    }

    public void setStore_points(String store_points) {
        this.store_points = store_points;
    }
}
