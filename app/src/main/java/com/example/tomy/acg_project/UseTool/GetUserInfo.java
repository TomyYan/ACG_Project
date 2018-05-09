package com.example.tomy.acg_project.UseTool;

import android.content.ContentValues;
import android.util.Log;
import com.example.tomy.acg_project.domain.Domain;
import com.example.tomy.acg_project.domain.HttpCallbackListener;
import com.example.tomy.acg_project.domain.HttpUnit;
import com.example.tomy.acg_project.domain.User;
import org.json.JSONException;
import org.json.JSONObject;

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
                //获取token
                send_token_get(Domain.getUserId(),Domain.getUserInfo().getAccount());
            }

            @Override
            public void onError(Exception e) {
                Log.e("GetUserInfo.class","ConnectError");
            }
        });
    }


    public void getOtherUserInfo(int userId) {
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
        Domain.getDb().update("token_table",values,"id=?",new String[]{"1"});
    }
}
