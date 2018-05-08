package com.example.tomy.acg_project.UseTool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.example.tomy.acg_project.domain.Domain;
import com.example.tomy.acg_project.domain.HttpCallbackListener;
import com.example.tomy.acg_project.domain.HttpUnit;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tomy on 18-5-7.
 */
public class ShowDeleteWindows {
    private String DeleteArticleAdress= Domain.Server_Address+"deleteArticle";
    private Activity activity;

    public ShowDeleteWindows(){}
    public ShowDeleteWindows(Activity activity){
        this.activity=activity;
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.obj.toString()){
                case "ok":
                    Toast.makeText(activity,"删除成功",Toast.LENGTH_SHORT).show();
                    break;
                case "fail":
                    Toast.makeText(activity,"删除失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    public void showIfDelete(){
        //显示删除的缩略信息
        String showMsg="";
        if(Domain.getArticleResponse().getArticle().length()>5){
            showMsg="是否删除"+"\""+Domain.getArticleResponse().getArticle().substring(0,5)+"...\"";
        }
        else{
            showMsg="是否删除"+"\""+Domain.getArticleResponse().getArticle()+"...\"";
        }
        // 这里的属性可以一直设置，因为每次设置后返回的是一个builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // 设置提示框的标题
        builder.setTitle("警告").
                // 设置提示框的图标
                //setIcon(R.drawable.ic_launcher).
                // 设置要显示的信息
                        setMessage(showMsg).
                // 设置确定按钮
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除文章
                        final JSONObject requestMsg=new JSONObject();
                        try {
                            requestMsg.put("articleId",Domain.getArticleResponse().getArticleId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        HttpUnit.postHttpRequest(requestMsg, DeleteArticleAdress, new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) throws JSONException {
                                JSONObject responseMsg=new JSONObject(response);
                                Message msg=new Message();
                                msg.obj=responseMsg.get("data");
                                handler.sendMessage(msg);
                            }
                            @Override
                            public void onError(Exception e) {
                                Log.e("deleteArticle","connectError");
                            }
                        });
                    }
                }).
                // 设置取消按钮,null是什么都不做
                        setNegativeButton("取消", null);
        // 生产对话框
        AlertDialog alertDialog = builder.create();
        // 显示对话框
        alertDialog.show();
    }
}
