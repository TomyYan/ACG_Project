package com.example.tomy.acg_project.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.example.tomy.acg_project.R;
import com.example.tomy.acg_project.UseTool.GetUserInfo;
import com.example.tomy.acg_project.domain.Domain;
import com.example.tomy.acg_project.domain.HttpCallbackListener;
import com.example.tomy.acg_project.domain.HttpUnit;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tomy on 18-3-9.
 */
public class Login_in extends Activity implements View.OnClickListener{

    private EditText Account_Edit,Keyword_Edit;
    private Button Login_in_Button,forgetPassword,register;
    private String address=Domain.Server_Address+"login";
    private String TokenLoginAddress=Domain.Server_Address+"AutoLogin";

    private String account,keyword;
    private String tokenString="";



    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.obj.toString()) {
                case "ok":
                    Intent intent=new Intent();
                    intent.setClass(Login_in.this,MainActivity.class);
                    Login_in.this.startActivity(intent);
                    //send_token_get(Domain.getUserId(),Domain.getUserInfo().getAccount());
                    finish();
                    break;
                case "fail":
                    Toast.makeText(Login_in.this, "账号密码错误", Toast.LENGTH_SHORT).show();
                    Account_Edit.setText("");
                    Keyword_Edit.setText("");
                    break;
                case "token_login_ok":
                    Intent tokenIntent=new Intent();
                    tokenIntent.setClass(Login_in.this,MainActivity.class);
                    Login_in.this.startActivity(tokenIntent);
                    //设置用户ID和信息

                    finish();
                default:
                        break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_in);
        Account_Edit=(EditText)findViewById(R.id.account_Edit);
        Keyword_Edit=(EditText)findViewById(R.id.keyword_Edit);
        Login_in_Button=(Button)findViewById(R.id.login_in_button);
        forgetPassword=(Button)findViewById(R.id.forget_keyword);
        register=(Button)findViewById(R.id.register);
        Login_in_Button.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        register.setOnClickListener(this);

        //使用SQLite查询token
        Cursor token;
        Domain.setDb(SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString()+"my.db3",null));
        Domain.setFilePath(this.getFilesDir().toString());
        try {
            token = Domain.getDb().rawQuery("select token from token_table where id=1", null);
            System.out.println("正确");
        }catch (SQLiteException e){
            System.out.println("出错");
            Domain.getDb().execSQL("create table token_table (id integer primary key,token varchar(300))");
            Domain.getDb().execSQL("insert into token_table (token) values(?)",new String[] {"i am tommy"});
            token = Domain.getDb().rawQuery("select token from token_table where id=1", null);
        }

        if (token.moveToFirst())
        {
            tokenString = token.getString(token.getColumnIndex("token"));
            System.out.println("获取的token为:"+tokenString);
        }
        send_token_login(tokenString);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_in_button:
                try {
                    System.out.println("点击");
                    send_login_in_msg();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.forget_keyword:
                Intent intentForget=new Intent();
                intentForget.setClass(Login_in.this,ForgetPassword.class);
                Login_in.this.startActivity(intentForget);
                break;
            case R.id.register:
                Intent intentRegister=new Intent();
                intentRegister.setClass(Login_in.this,Register.class);
                Login_in.this.startActivity(intentRegister);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //关闭数据库
        if(Domain.getDb()!=null&&Domain.getDb().isOpen()){
            Domain.getDb().close();
        }
    }
    public void send_login_in_msg() throws JSONException {
        account=Account_Edit.getText().toString();
        keyword=Keyword_Edit.getText().toString();
        if(account.equals("")||keyword.equals("")){
            return;
        }
        final JSONObject requestMsg=new JSONObject();
        //requestMsg.put("operation","login_in");
        requestMsg.put("account",account);
        requestMsg.put("keyword",keyword);
        HttpUnit.postHttpRequest(requestMsg, address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject responseMsg=new JSONObject(response);
                //处理登录返回信息
                String result=responseMsg.getString("result");
                Domain.setUserId(Integer.parseInt(responseMsg.getString("userId")));
                if(Domain.getUserId()!=0){
                    new GetUserInfo().getUserInfo(Domain.getUserId());
                }
                System.out.println(result);
                //System.out.println(responseMsg.toString());
                Message msg=new Message();
                msg.obj=result;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                //测试用
                /*
                Message msg=new Message();
                msg.obj="ok";
                handler.sendMessage(msg);
                */
                //
                Log.e("Login_in.java","Connect_Error");
                //Toast.makeText(Login_in.this,"服务器炸啦*_*",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void send_token_login(String token){
        final JSONObject requestMsg=new JSONObject();
        try {
            requestMsg.put("token",token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUnit.postHttpRequest(requestMsg, TokenLoginAddress, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                String result="";
                JSONObject responseMsg=new JSONObject(response);
                System.out.println("接收信息为:"+responseMsg);
                if("1001".equals(responseMsg.getString("code"))){
                    result="token_login_ok";
                    //设置userID
                    Domain.setUserId(Integer.parseInt(responseMsg.getJSONObject("data").getString("uid")));
                    if(Domain.getUserId()!=0){
                        new GetUserInfo().getUserInfo(Domain.getUserId());
                    }
                }else{
                    result="token_login_fail";
                }
                System.out.println(result);
                Message msg=new Message();
                msg.obj=result;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                Log.e("TokenLoginIn","ErrorConnect");
            }
        });
    }


}
