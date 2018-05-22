package com.example.tomy.acg_project.UseTool;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.example.tomy.acg_project.domain.Domain;
import com.example.tomy.acg_project.domain.HttpCallbackListener;
import com.example.tomy.acg_project.domain.HttpUnit;
import com.example.tomy.acg_project.domain.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tomy on 18-5-1.
 */
public class GetUserInfo {
    private static final String address= Domain.Server_Address+"getUserInfo";
    private String getTokenAddress=Domain.Server_Address+"getToken";
    public void getUserInfo(int userId) {
        JSONObject request = new JSONObject();
        try {
            request.put("userId",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUnit.postHttpRequest(request, address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject responseMsg=new JSONObject(response);
                User userInfo=new User();
                userInfo.setAccount(responseMsg.getString("account"));
                userInfo.setAccountImg(responseMsg.getString("accountImg"));
                userInfo.setAccountSex(responseMsg.getString("accountSex"));
                userInfo.setAccountSign(responseMsg.getString("accountSign"));
                userInfo.setEmail(responseMsg.getString("email"));
                userInfo.setUserName(responseMsg.getString("userName"));
                userInfo.setIsAdmin(Integer.parseInt(responseMsg.getString("isAdmin")));
                System.out.println("是否管理员:"+Integer.parseInt(responseMsg.getString("isAdmin")));
                Domain.setUserInfo(userInfo);
                try {
                    getBitmap(Domain.getUserId()+"");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //获取token
                send_token_get(Domain.getUserId(),Domain.getUserInfo().getAccount());
            }

            @Override
            public void onError(Exception e) {
                Log.e("GetUserInfo.class","ConnectError");
            }
        });
    }

    public void getOtherUserInfo(final int userId) {
        JSONObject request = new JSONObject();
        try {
            request.put("userId",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUnit.postHttpRequest(request, address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject responseMsg=new JSONObject(response);
                User userInfo=new User();
                userInfo.setAccount(responseMsg.getString("account"));
                userInfo.setAccountImg(responseMsg.getString("accountImg"));
                userInfo.setAccountSex(responseMsg.getString("accountSex"));
                userInfo.setAccountSign(responseMsg.getString("accountSign"));
                userInfo.setEmail(responseMsg.getString("email"));
                userInfo.setUserName(responseMsg.getString("userName"));
                Domain.setOtherInfo(userInfo);
                try {
                    getOtherBitmap(userId+"");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("GetUserInfo.class","ConnectError");
            }
        });
    }

    public void send_token_get(int userId,String userAccount){
        JSONObject requestMsg=new JSONObject();
        try {
            requestMsg.put("userId",userId);
            requestMsg.put("userAccount",userAccount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUnit.postHttpRequest(requestMsg, getTokenAddress, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                System.out.println("接收到信息为:"+response);
                JSONObject responseMsg=new JSONObject(response);
                String result=responseMsg.getString("data");
                System.out.println("获取token为:"+result);
                updateSQLite(result);
            }

            @Override
            public void onError(Exception e) {
                Log.e("getToken","ErrorConnect");
            }
        });
    }

    public void updateSQLite(String token){
        //SQLiteDatabase db = mySql.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("token",token);
        if(Domain.getDb()!=null&&Domain.getDb().isOpen()) {
            Domain.getDb().update("token_table", values, "id=?", new String[]{"1"});
        }else{
            Domain.setDb(SQLiteDatabase.openOrCreateDatabase(Domain.getFilePath()+"my.db3",null));
            Domain.getDb().update("token_table", values, "id=?", new String[]{"1"});
            if(Domain.getDb()!=null&&Domain.getDb().isOpen()){
                Domain.getDb().close();
            }
        }
    }

    public static void getBitmap(final String ID) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(Domain.Server_Address+"images/photo_"+ID+".png");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    if(conn.getResponseCode() == 200){
                        //System.out.println("ok");
                        InputStream inputStream = conn.getInputStream();
                        Domain.img = BitmapFactory.decodeStream(inputStream);
                        return;
                    }
                }catch (Exception e){
                    Log.e("error","error");
                    System.out.println(e);
                }
            }
        }).start();
        return;
    }

    public static void getOtherBitmap(final String ID) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(Domain.Server_Address+"images/photo_"+ID+".png");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    if(conn.getResponseCode() == 200){
                        //System.out.println("ok");
                        InputStream inputStream = conn.getInputStream();
                        Domain.othersImg = BitmapFactory.decodeStream(inputStream);
                        return;
                    }
                }catch (Exception e){
                    Log.e("error","error");
                    System.out.println(e);
                }
            }
        }).start();
        return;
    }
}
