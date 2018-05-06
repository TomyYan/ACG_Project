package com.example.tomy.acg_project.UseTool;

import com.example.tomy.acg_project.domain.Domain;
import com.example.tomy.acg_project.domain.CommentResponse;
import com.example.tomy.acg_project.domain.HttpCallbackListener;
import com.example.tomy.acg_project.domain.HttpUnit;
import com.example.tomy.acg_project.view.DetailArticle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tomy on 18-4-15.
 */
public class GetComment {
    private final static String address= Domain.Server_Address+"getComment";
    private ArrayList<CommentResponse> comment=new ArrayList<>();
    public void getComment(int articleId){
        final JSONObject requestMsg=new JSONObject();
        try {
            requestMsg.put("articleId",articleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUnit.postHttpRequest(requestMsg, address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                System.out.println("获取评论:"+response);
                JSONObject responseMsg=new JSONObject(response);
                JSONArray responseMsgComment=new JSONArray(responseMsg.getString("data"));
                for(int i=0;i<responseMsgComment.length();i++){
                    JSONObject commentJsonUnit=new JSONObject(responseMsgComment.get(i).toString());
                    CommentResponse commentResponse=new CommentResponse();
                    commentResponse.setUserId(commentJsonUnit.getInt("userId"));
                    commentResponse.setArticleComment(commentJsonUnit.getString("articleComment"));
                    commentResponse.setUserName(commentJsonUnit.getString("userName"));
                    commentResponse.setAccountImg(commentJsonUnit.getString("accountImg"));
                    //comment.add(commentResponse);
                    DetailArticle.update(commentResponse);
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
