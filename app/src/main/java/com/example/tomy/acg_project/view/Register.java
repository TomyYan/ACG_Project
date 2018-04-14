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
public class Register extends AppCompatActivity implements View.OnClickListener{

    private EditText nameEdit,accountEdit,passwordInputEdit,passwordInputEditTwo,emailInputEdit;
    private Button registerButton;
    private String address= Domain.Server_Address+"register";

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.obj.toString()){
                case "ok":
                    Toast.makeText(Register.this,"注册成功",Toast.LENGTH_SHORT).show();
                    Register.this.finish();
                    break;
                case "fail":
                    Toast.makeText(Register.this,"注册失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        nameEdit=(EditText)findViewById(R.id.NameEdit);
        accountEdit=(EditText)findViewById(R.id.accountEdit);
        passwordInputEdit=(EditText)findViewById(R.id.passwordInputEdit);
        passwordInputEditTwo=(EditText)findViewById(R.id.passwordInputEditTwo);
        emailInputEdit=(EditText)findViewById(R.id.emailInputEdit);
        registerButton=(Button)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerButton:
                if(passwordInputEdit.getText().toString().equals(passwordInputEditTwo.getText().toString())){
                    send_register_msg();
                }else{
                    Toast.makeText(this,"密码输入不一致",Toast.LENGTH_SHORT).show();
                    passwordInputEdit.setText("");
                    passwordInputEditTwo.setText("");
                }
                break;
            default:
                break;
        }
    }

    public void send_register_msg(){
        final JSONObject registerMsg=new JSONObject();
        try {
            registerMsg.put("userName",nameEdit.getText().toString());
            registerMsg.put("account",accountEdit.getText().toString());
            registerMsg.put("password",passwordInputEdit.getText().toString());
            registerMsg.put("email",emailInputEdit.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUnit.postHttpRequest(registerMsg, address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject responseMsg=new JSONObject(response);
                Message msg=new Message();
                String result=responseMsg.getString("data");
                msg.obj=result;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                Log.e("Register.class","connectError");
            }
        });
    }
}
