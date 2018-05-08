package com.example.tomy.acg_project.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.example.tomy.acg_project.R;
import com.example.tomy.acg_project.domain.Domain;

/**
 * Created by tomy on 18-5-6.
 */
public class UserInfo extends AppCompatActivity{

    private EditText othersAccountInput,othersNickNameInput,othersSexInput,othersEmailInput,othersSignInput;
    private ImageButton imgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        //获取界面组件
        othersAccountInput=(EditText)findViewById(R.id.othersAccountInput);
        othersNickNameInput=(EditText)findViewById(R.id.othersNickNameInput);
        othersSexInput=(EditText)findViewById(R.id.othersSexInput);
        othersEmailInput=(EditText)findViewById(R.id.othersEmailInput);
        othersSignInput=(EditText)findViewById(R.id.othersSignInput);
        imgButton=(ImageButton)findViewById(R.id.imgButton);
        //初始化界面
        initUserInfo();
    }

    //设置用户信息
    public void initUserInfo(){
        othersAccountInput.setText(Domain.getOtherInfo().getAccount());
        othersNickNameInput.setText(Domain.getOtherInfo().getUserName());
        othersSexInput.setText(Domain.getOtherInfo().getAccountSex());
        othersEmailInput.setText(Domain.getOtherInfo().getEmail());
        othersSignInput.setText(Domain.getOtherInfo().getAccountSign());
    }
}
