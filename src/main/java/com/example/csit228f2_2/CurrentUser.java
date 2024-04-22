package com.example.csit228f2_2;

public class CurrentUser {
    private static int userID;
    private static String username;

    public CurrentUser(){}

    public static void setUserID(int userID) {
        CurrentUser.userID = userID;
    }

    public static void setUsername(String username) {
        CurrentUser.username = username;
    }

    public static int getUserID() {
        return userID;
    }

    public static String getUsername() {
        return username;
    }
}
