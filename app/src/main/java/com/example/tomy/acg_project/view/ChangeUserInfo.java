package com.example.tomy.acg_project.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.tomy.acg_project.R;
import com.example.tomy.acg_project.domain.Domain;
import com.example.tomy.acg_project.domain.HttpCallbackListener;
import com.example.tomy.acg_project.domain.HttpUnit;
import com.example.tomy.acg_project.domain.User;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tomy on 18-5-6.
 */
public class ChangeUserInfo extends AppCompatActivity implements View.OnClickListener{

    private String address=Domain.Server_Address+"setUserInfo";
    private EditText accountInput,nickNameInput,sexInput,meEmailInput,meSignInput;
    private ImageButton imgButton;
    private Button sureChange;

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.obj.toString()){
                case "ok":
                    Toast.makeText(ChangeUserInfo.this,"ok",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case "fail":
                    Toast.makeText(ChangeUserInfo.this,"fail",Toast.LENGTH_SHORT).show();
                    break;
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_change);

        //获取界面组件
        accountInput=(EditText)findViewById(R.id.accountInput);
        nickNameInput=(EditText)findViewById(R.id.nickNameInput);
        sexInput=(EditText)findViewById(R.id.sexInput);
        meEmailInput=(EditText)findViewById(R.id.meEmailInput);
        meSignInput=(EditText)findViewById(R.id.meSignInput);
        imgButton=(ImageButton)findViewById(R.id.imgButton);
        sureChange=(Button)findViewById(R.id.changeSure);

        //
        sureChange.setOnClickListener(this);

        //初始化界面
        initUserInfo();
    }

    //设置用户信息
    public void initUserInfo(){
        accountInput.setText(Domain.getUserInfo().getAccount());
        nickNameInput.setText(Domain.getUserInfo().getUserName());
        sexInput.setText(Domain.getUserInfo().getAccountSex());
        meEmailInput.setText(Domain.getUserInfo().getEmail());
        meSignInput.setText(Domain.getUserInfo().getAccountSign());
    }

    //更新用户信息
    public void changeInfo(){
        Domain.getUserInfo().setAccountSex(sexInput.getText().toString());
        Domain.getUserInfo().setAccountSign(meSignInput.getText().toString());
        Domain.getUserInfo().setEmail(meEmailInput.getText().toString());
        Domain.getUserInfo().setUserName(nickNameInput.getText().toString());
    }
    //修改服务器用户信息
    public void changeServiceInfo(){
        JSONObject request=new JSONObject();
        try {
            request.put("userId",Domain.getUserId());
            request.put("userName",Domain.getUserInfo().getUserName());
            request.put("email",Domain.getUserInfo().getEmail());
            request.put("accountSex",Domain.getUserInfo().getAccountSex());
            request.put("accountSign",Domain.getUserInfo().getAccountSign());
            request.put("accountImg",Domain.getUserInfo().getAccountImg());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUnit.postHttpRequest(request, address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject responseMsg=new JSONObject(response);
                String result=responseMsg.getString("data");
                Message msg=new Message();
                msg.obj=result;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                Log.e("ChangeUserInfo.class","connectError");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.changeSure:
                System.out.println("change");
                //更新程序保存信息
                changeInfo();
                //上传到服务端更新
                changeServiceInfo();
                break;
            default:break;
        }
    }
}
