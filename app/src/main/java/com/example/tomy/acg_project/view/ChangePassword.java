package com.example.tomy.acg_project.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.tomy.acg_project.R;
import com.example.tomy.acg_project.domain.Domain;
import com.example.tomy.acg_project.domain.HttpCallbackListener;
import com.example.tomy.acg_project.domain.HttpUnit;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tomy on 18-5-7.
 */
public class ChangePassword extends AppCompatActivity implements View.OnClickListener{

    private EditText inputOldPassword,inputNewPassword,inputNewPasswordAgain;
    private Button sureToChangePassword;
    private String oldPassword,newPassword,newPasswordAgein;
    private String changePasswordAddress= Domain.Server_Address+"updatePassword";

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.obj.toString()){
                case "ok":
                    Toast.makeText(ChangePassword.this,"修改成功",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case "fail":
                    Toast.makeText(ChangePassword.this,"修改失败,请重试",Toast.LENGTH_SHORT).show();
                    break;
                case "oldPasswordFail":
                    Toast.makeText(ChangePassword.this,"旧密码输入错误",Toast.LENGTH_SHORT).show();
                    inputOldPassword.setText("");
                    inputOldPassword.requestFocus();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        //获取界面组件
        inputOldPassword=(EditText)findViewById(R.id.inputOldPassword);
        inputNewPassword=(EditText)findViewById(R.id.inputNewPassword);
        inputNewPasswordAgain=(EditText)findViewById(R.id.inputNewPasswordAgain);
        sureToChangePassword=(Button)findViewById(R.id.sureToChangePassword);

        //设置按钮监听
        sureToChangePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sureToChangePassword:
                sendChangePasswordRequest();
                break;
            default:
                break;
        }
    }

    public void sendChangePasswordRequest(){
        oldPassword=inputOldPassword.getText().toString();
        newPassword=inputNewPassword.getText().toString();
        newPasswordAgein=inputNewPasswordAgain.getText().toString();
        if(oldPassword.equals("")||newPassword.equals("")||newPasswordAgein.equals("")){
            Toast.makeText(ChangePassword.this,"输入不能为空",Toast.LENGTH_SHORT).show();
        }else{
            if(newPassword.equals(newPasswordAgein)){
                JSONObject requestMsg=new JSONObject();
                try {
                    requestMsg.put("userId",Domain.getUserId());
                    requestMsg.put("oldPassword",oldPassword);
                    requestMsg.put("newPassword",newPassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HttpUnit.postHttpRequest(requestMsg, changePasswordAddress, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) throws JSONException {
                        JSONObject responseMsg=new JSONObject(response);
                        Message msg=new Message();
                        msg.obj=responseMsg.get("data");
                        System.out.println("接收"+msg.obj.toString());
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("ChangePassword.class","ConnectError");
                    }
                });
            }else{
                Toast.makeText(ChangePassword.this,"新密码输入不一致",Toast.LENGTH_SHORT).show();
            }
        }
        return;
    }
}
