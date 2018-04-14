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
 * Created by tomy on 18-4-13.
 */
public class ForgetPassword extends AppCompatActivity implements View.OnClickListener{

    private EditText forgetAccountInput,forgetEmailInput;
    private Button forgetSendEmail;
    private String address= Domain.Server_Address+"forgetPassword";

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.obj.toString()){
                case "ok":
                    Toast.makeText(ForgetPassword.this,"邮件已发送，请查收",Toast.LENGTH_SHORT).show();
                    ForgetPassword.this.finish();
                    break;
                case "fail":
                    Toast.makeText(ForgetPassword.this,"账号或邮箱错误",Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        forgetAccountInput=(EditText)findViewById(R.id.forgetAccountInput);
        forgetEmailInput=(EditText)findViewById(R.id.forgetEmailInput);
        forgetSendEmail=(Button)findViewById(R.id.forgetSendEmail);
        forgetSendEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forgetSendEmail:
                sendForgetEmail();
                break;
            default:
                break;
        }
    }

    public void sendForgetEmail(){
        final JSONObject forgetMsg=new JSONObject();
        try {
            forgetMsg.put("account",forgetAccountInput.getText().toString());
            forgetMsg.put("email",forgetEmailInput.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUnit.postHttpRequest(forgetMsg, address, new HttpCallbackListener() {
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
                Log.e("ForgetPassword.class","connectError");
            }
        });
    }

}
