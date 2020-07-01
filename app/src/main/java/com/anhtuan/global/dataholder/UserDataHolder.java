package com.anhtuan.global.dataholder;

import com.anhtuan.pojo.User;

public class UserDataHolder {
    private static User currentUser;

    public static User getUser() {
        if (currentUser == null) {
            currentUser = new User();
        }
        return currentUser;
    }

    public static void updateUser(User user) {
        getUser().setUserId(user.getUserId());
        getUser().setUserName(user.getUserName());
        getUser().setUserShortName(user.getUserShortName());
    }
}
