package com.example.tomy.acg_project.domain;

/**
 * Created by tomy on 18-3-9.
 */
public class Domain {
    public static final String Server_Address="http://172.22.30.48:8080/";
    private static int userId;

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        Domain.userId = userId;
    }
}
