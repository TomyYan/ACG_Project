package com.example.tomy.acg_project.domain;

import java.io.Serializable;

/**
 * Created by tomy on 18-1-28.
 */
public class User implements Serializable{
    private int userId;
    private String account;
    private String password;
    private String email;
    private String userName;
    private String accountSex="";
    private String accountSign="";
    private String accountImg="";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccountSex() {
        return accountSex;
    }

    public void setAccountSex(String accountSex) {
        this.accountSex = accountSex;
    }

    public String getAccountSign() {
        return accountSign;
    }

    public void setAccountSign(String accountSign) {
        this.accountSign = accountSign;
    }

    public String getAccountImg() {
        return accountImg;
    }

    public void setAccountImg(String accountImg) {
        this.accountImg = accountImg;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

}
