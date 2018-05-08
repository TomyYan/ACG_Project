package com.example.tomy.acg_project.UseTool;

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
}
