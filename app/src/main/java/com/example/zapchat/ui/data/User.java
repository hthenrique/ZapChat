package com.example.zapchat.ui.data;

public class User {

    private String uuid;
    private String username;
    private String email;
    private String profileUrl;

    public User(){
    }

    public User(String uuid, String username,String email, String profileUrl) {
        this.uuid = uuid;
        this.username = username;
        this.profileUrl = profileUrl;
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getEmail(){
        return email;
    }

}
