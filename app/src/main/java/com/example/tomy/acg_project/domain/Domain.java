package com.example.tomy.acg_project.domain;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

/**
 * Created by tomy on 18-3-9.
 */
public class Domain {
    //public static final String Server_Address="http://172.22.30.48:8080/";
    //public static final String Server_Address="http://192.168.43.144:8080/";
    public static final String Server_Address="http://192.168.1.101:8080/";
    private static int userId;
    private static User userInfo;
    private static User otherInfo=new User();
    private static SQLiteDatabase db;
    private static String tokenSQL="";
    private static Activity mainActivity;
    private static String filePath;
    public static Bitmap img=null;
    public static Bitmap othersImg=null;
    public static updataPhoto updata=null;

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        Domain.filePath = filePath;
    }

    public static Activity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(Activity mainActivity) {
        Domain.mainActivity = mainActivity;
    }

    public static String getToken() {
        return tokenSQL;
    }

    public static void setToken(String tokenSQL) {
        Domain.tokenSQL = tokenSQL;
    }
    public static SQLiteDatabase getDb() {
        return db;
    }

    public static void setDb(SQLiteDatabase db) {
        Domain.db = db;
    }
    public static User getOtherInfo() {
        return otherInfo;
    }

    public static void setOtherInfo(User otherInfo) {
        Domain.otherInfo = otherInfo;
    }

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
