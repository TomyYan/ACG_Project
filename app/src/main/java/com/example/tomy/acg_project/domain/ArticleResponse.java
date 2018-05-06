package com.example.tomy.acg_project.domain;

/**
 * Created by tomy on 18-4-14.
 */
public class ArticleResponse {
    private int userId;
    private String userName;
    private int articleId;
    private String article;
    private int thumbsUpNum;
    private int thumbsDownNum;
    private String articleImgAddress;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public int getThumbsUpNum() {
        return thumbsUpNum;
    }

    public void setThumbsUpNum(int thumbsUpNum) {
        this.thumbsUpNum = thumbsUpNum;
    }

    public int getThumbsDownNum() {
        return thumbsDownNum;
    }

    public void setThumbsDownNum(int thumbsDownNum) {
        this.thumbsDownNum = thumbsDownNum;
    }

    public String getArticleImgAddress() {
        return articleImgAddress;
    }

    public void setArticleImgAddress(String articleImgAddress) {
        this.articleImgAddress = articleImgAddress;
    }



}
