package com.example.tomy.acg_project.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.tomy.acg_project.R;
import com.example.tomy.acg_project.domain.Domain;
import com.example.tomy.acg_project.domain.HttpCallbackListener;
import com.example.tomy.acg_project.domain.HttpUnit;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tomy on 18-3-28.
 */
public class AddArticle extends AppCompatActivity implements View.OnClickListener{
    private RadioGroup select_type;
    private Button publish;
    private EditText articleEdit;
    private int type=0;
    private String article;
    private String address=Domain.Server_Address+"publishArticle";
    //private String user;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.obj.toString()) {
                case "ok":
                    Toast.makeText(AddArticle.this, "发布成功", Toast.LENGTH_SHORT).show();
                    AddArticle.this.finish();
                    break;
                case "fail":
                    Toast.makeText(AddArticle.this, "发布失败", Toast.LENGTH_SHORT).show();
                    break;
                case "hasSensitivity":
                    Toast.makeText(AddArticle.this,"含有敏感词，请重新输入",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_article);
        //Intent intent=getIntent();
        //user=intent.getStringExtra("user");
        publish=(Button)findViewById(R.id.publish);
        publish.setOnClickListener(AddArticle.this);
        articleEdit=(EditText)findViewById(R.id.articleEdit);
        select_type=(RadioGroup)findViewById(R.id.select_type);
        //select_type.check(R.id.select_a);
        select_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.select_a:
                        type=1;
                        break;
                    case R.id.select_c:
                        type=2;
                        break;
                    case R.id.select_g:
                        type=3;
                        break;
                    default:
                        type=0;
                        break;
                }
                //Toast.makeText(AddArticle.this,"选择id为:"+type,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.publish:
                if("".equals(articleEdit.getText())||type==0){
                    Toast.makeText(AddArticle.this,"输入类型或内容不能为空",Toast.LENGTH_SHORT).show();
                }
                else{
                    final JSONObject articleMsg=new JSONObject();
                    try {
                        articleMsg.put("userId",Domain.getUserId());
                        articleMsg.put("type",type);
                        articleMsg.put("article",articleEdit.getText());
                        HttpUnit.postHttpRequest(articleMsg, address, new HttpCallbackListener() {
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
                                Log.e("AddArticle.class","connectError");
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}
