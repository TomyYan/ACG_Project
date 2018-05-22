package com.example.tomy.acg_project.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.tomy.acg_project.R;
import com.example.tomy.acg_project.UseTool.GetUserInfo;
import com.example.tomy.acg_project.domain.Domain;

/**
 * Created by tomy on 18-5-21.
 */
public class Setting extends AppCompatActivity implements View.OnClickListener{
    private Button exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        exit=(Button)findViewById(R.id.exit);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exit:
                //修改token
                new GetUserInfo().updateSQLite("ACG");
                //转到登录界面
                Intent intent=new Intent();
                intent.setClass(Setting.this,Login_in.class);
                Setting.this.startActivity(intent);
                //关闭setting页面
                finish();
                //关闭主页面
                Domain.getMainActivity().finish();
                break;
            default:
                break;
        }
    }
}
