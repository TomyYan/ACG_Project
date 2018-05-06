package com.example.tomy.acg_project.UseTool;

import android.util.Log;
import com.example.tomy.acg_project.Fragment.AFragment;
import com.example.tomy.acg_project.Fragment.CFragment;
import com.example.tomy.acg_project.Fragment.GFragment;
import com.example.tomy.acg_project.Fragment.MeFragment;
import com.example.tomy.acg_project.adapter.MyAdapter;
import com.example.tomy.acg_project.domain.ArticleResponse;
import com.example.tomy.acg_project.domain.Domain;
import com.example.tomy.acg_project.domain.HttpCallbackListener;
import com.example.tomy.acg_project.domain.HttpUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tomy on 18-4-14.
 */
public class GetArticle {
    //private MyAdapter adapter;
    private static final String address= Domain.Server_Address+"getArticle";
    private static final String addressUserArticle= Domain.Server_Address+"getUserArticle";
    //private ArrayList<ArticleResponse> article=new ArrayList<>();

    public GetArticle(){}
    //public GetArticle(MyAdapter adapter){this.adapter=adapter;}
    public void getArticle(final int type, int start){
        JSONObject request = new JSONObject();
        try {
            request.put("type",type);
            request.put("start",start);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUnit.postHttpRequest(request, address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                System.out.println("获取文章得到："+response);
                //JSONObject responseMsg=new JSONObject(response);
                //JSONArray responseMsg=new JSONArray(response);
                JSONObject responseData=new JSONObject(response);
                //System.out.println(responseMsg.get(0));
                JSONArray articleMsg=new JSONArray(responseData.getString("data"));
                //System.out.println("文章内容为："+articleMsg.get(0));
                for(int i=0;i<articleMsg.length();i++){
                    System.out.println("次数"+articleMsg.length());
                    JSONObject articleUnit=new JSONObject(articleMsg.get(i).toString());
                    ArticleResponse articleResponse=new ArticleResponse();
                    articleResponse.setArticle(articleUnit.getString("article"));
                    articleResponse.setArticleId(articleUnit.getInt("articleId"));
                    articleResponse.setThumbsDownNum(Integer.parseInt(articleUnit.getString("thumbsDownNum")));
                    articleResponse.setThumbsUpNum(Integer.parseInt(articleUnit.getString("thumbsUpNum")));
                    articleResponse.setUserId(Integer.parseInt(articleUnit.getString("userId")));
                    articleResponse.setUserName(articleUnit.getString("userName"));
                    //article.add(articleResponse);
                    if(type==1){
                        AFragment.updateView(articleResponse);
                    }else if(type==2){
                        CFragment.updateView(articleResponse);
                    }else if(type==3){
                        GFragment.updateView(articleResponse);
                    }

                    System.out.println("加载...");
                }
                //System.out.println("文章长度为:"+article.size());
                //adapter.updateData(article);
            }

            @Override
            public void onError(Exception e) {
                Log.e("GetArticle.class","connectError");
            }
        });
        return;
    }
    public void getUserArticle(int userId, int start){
        JSONObject request = new JSONObject();
        try {
            request.put("userId",userId);
            request.put("start",start);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUnit.postHttpRequest(request, addressUserArticle, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                System.out.println("获取文章得到："+response);
                //JSONObject responseMsg=new JSONObject(response);
                //JSONArray responseMsg=new JSONArray(response);
                JSONObject responseData=new JSONObject(response);
                //System.out.println(responseMsg.get(0));
                JSONArray articleMsg=new JSONArray(responseData.getString("data"));
                //System.out.println("文章内容为："+articleMsg.get(0));
                for(int i=0;i<articleMsg.length();i++){
                    System.out.println("次数"+articleMsg.length());
                    JSONObject articleUnit=new JSONObject(articleMsg.get(i).toString());
                    ArticleResponse articleResponse=new ArticleResponse();
                    articleResponse.setArticle(articleUnit.getString("article"));
                    articleResponse.setArticleId(articleUnit.getInt("articleId"));
                    articleResponse.setThumbsDownNum(Integer.parseInt(articleUnit.getString("thumbsDownNum")));
                    articleResponse.setThumbsUpNum(Integer.parseInt(articleUnit.getString("thumbsUpNum")));
                    articleResponse.setUserId(Integer.parseInt(articleUnit.getString("userId")));
                    articleResponse.setUserName(articleUnit.getString("userName"));
                    //article.add(articleResponse);

                    MeFragment.updateView(articleResponse);

                    System.out.println("加载...");
                }
                //System.out.println("文章长度为:"+article.size());
                //adapter.updateData(article);
            }

            @Override
            public void onError(Exception e) {
                Log.e("GetArticle.class","connectError");
            }
        });
        return;
    }
}
