package com.example.tomy.acg_project.domain;

/**
 * Created by tomy on 18-3-9.
 */
public class Domain {
    //public static final String Server_Address="http://172.22.30.48:8080/";
    //public static final String Server_Address="http://192.168.43.144:8080/";
    public static final String Server_Address="http://192.168.1.102:8080/";
    private static int userId;
    private static User userInfo;

    public static User getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(User userInfo) {
        Domain.userInfo = userInfo;
    }



    public static ArticleResponse getArticleResponse() {
        return articleResponse;
    }

    public static void setArticleResponse(ArticleResponse articleResponse) {
        Domain.articleResponse = articleResponse;
    }

    private static ArticleResponse articleResponse;


    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        Domain.userId = userId;
    }
}
