package com.anhtuan.pojo;

public class User {
    private String userId;
    private String userShortName;
    private String userName;

    public User() {
        this.userId = "";
        this.userName = "";
        this.userShortName = "";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserShortName() {
        return userShortName;
    }

    public void setUserShortName(String userShortName) {
        this.userShortName = userShortName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
