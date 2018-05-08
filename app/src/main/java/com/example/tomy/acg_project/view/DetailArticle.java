package com.example.tomy.acg_project.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.tomy.acg_project.Fragment.AFragment;
import com.example.tomy.acg_project.R;
import com.example.tomy.acg_project.UseTool.GetComment;
import com.example.tomy.acg_project.UseTool.GetUserInfo;
import com.example.tomy.acg_project.adapter.CommentAdapter;
import com.example.tomy.acg_project.adapter.MyAdapter;
import com.example.tomy.acg_project.domain.CommentResponse;
import com.example.tomy.acg_project.domain.Domain;
import com.example.tomy.acg_project.domain.HttpCallbackListener;
import com.example.tomy.acg_project.domain.HttpUnit;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tomy on 18-4-15.
 */
public class DetailArticle extends AppCompatActivity implements View.OnClickListener{

    private ImageButton imageShow,thumbsUpImg,thumbsDownImg,commentImg;
    private TextView authorName,article,thumbsUpNum,thumbsDownNum;
    private LinearLayout commentLinear;
    private EditText commentEdit;
    private Button publicComment;
    private static TextView commentNumShow;
    private RecyclerView rv;
    private static int commentNum=0;
    private static CommentAdapter commentAdapter;
    private static Activity activity;
    private String thumbsUpAddress=Domain.Server_Address+"pointThumbsUp";
    private String thumbsDownAddress=Domain.Server_Address+"pointThumbsDown";
    private String publishCommentAddress=Domain.Server_Address+"publishComment";

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.obj.toString()){
                case "okThumbsUp":
                    Toast.makeText(DetailArticle.this,"点赞成功",Toast.LENGTH_SHORT).show();
                    Domain.getArticleResponse().setThumbsUpNum(Domain.getArticleResponse().getThumbsUpNum()+1);
                    thumbsUpNum.setText(Domain.getArticleResponse().getThumbsUpNum()+"");
                    break;
                case "failThumbsUp":
                    Toast.makeText(DetailArticle.this,"点赞失败",Toast.LENGTH_SHORT).show();
                    break;
                case "hadPointThumbsUp":
                    Toast.makeText(DetailArticle.this,"已赞,不能重复点赞",Toast.LENGTH_SHORT).show();
                    break;
                case "okThumbsDown":
                    Toast.makeText(DetailArticle.this,"点踩成功",Toast.LENGTH_SHORT).show();
                    Domain.getArticleResponse().setThumbsDownNum(Domain.getArticleResponse().getThumbsDownNum()+1);
                    thumbsDownNum.setText(Domain.getArticleResponse().getThumbsDownNum()+"");
                    break;
                case "failThumbsDown":
                    Toast.makeText(DetailArticle.this,"点踩失败",Toast.LENGTH_SHORT).show();
                    break;
                case "hadPointThumbsDown":
                    Toast.makeText(DetailArticle.this,"已踩,不能重复点踩",Toast.LENGTH_SHORT).show();
                    break;
                case "okPublicComment":
                    Toast.makeText(DetailArticle.this,"评论成功",Toast.LENGTH_SHORT).show();
                    commentEdit.setText("");
                    commentLinear.setVisibility(-1);
                    break;
                case "failPublicComment":
                    Toast.makeText(DetailArticle.this,"评论失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_article);

        activity=this;

        imageShow=(ImageButton)findViewById(R.id.imageShow);
        thumbsUpImg=(ImageButton)findViewById(R.id.thumbsUpImg);
        thumbsDownImg=(ImageButton)findViewById(R.id.thumbsDownImg);
        commentImg=(ImageButton)findViewById(R.id.commentImg);
        authorName=(TextView)findViewById(R.id.authorName);
        article=(TextView)findViewById(R.id.article);
        thumbsUpNum=(TextView)findViewById(R.id.thumbsUpNum);
        thumbsDownNum=(TextView)findViewById(R.id.thumbsDownNum);
        commentNumShow=(TextView)findViewById(R.id.commentNumShow);
        commentLinear=(LinearLayout)findViewById(R.id.commentLinear);
        commentEdit=(EditText)findViewById(R.id.commentEdit);
        publicComment=(Button)findViewById(R.id.publicComment);

        authorName.setText(Domain.getArticleResponse().getUserName());
        article.setText(Domain.getArticleResponse().getArticle());
        thumbsUpNum.setText(Domain.getArticleResponse().getThumbsUpNum()+"");
        thumbsDownNum.setText(Domain.getArticleResponse().getThumbsDownNum()+"");

        imageShow.setOnClickListener(this);
        thumbsUpImg.setOnClickListener(this);
        thumbsDownImg.setOnClickListener(this);
        commentImg.setOnClickListener(this);
        publicComment.setOnClickListener(this);

        //System.out.println("获取文章ID为："+ Domain.getArticleResponse().getArticleId());
        //System.out.println("文章内容为:"+Domain.getArticleResponse().getArticle());

        //Toast.makeText(this,"文章Id为：",Toast.LENGTH_SHORT).show();
        rv = (RecyclerView)findViewById(R.id.comment_view);

        //设置recyclerview的布局样式
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //将布局设置为表格布局，3为3列
        // rv.setLayoutManager(new GridLayoutManager(getContext(),3,GridLayoutManager.VERTICAL,false));
        //设置recyclerview的分隔线 DividerItemDecoration是一个独立的文件
        //rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        commentAdapter = new CommentAdapter(new ArrayList<CommentResponse>());
        rv.setAdapter(commentAdapter);
        commentNum=0;

        //
        new GetComment().getComment(Domain.getArticleResponse().getArticleId());
        //
        new GetUserInfo().getOtherUserInfo(Domain.getArticleResponse().getUserId());
    }

    public static void update(CommentResponse commentResponse){
        commentAdapter.updateData(commentResponse);
        commentNum++;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                commentNumShow.setText("评论数为:"+commentNum);
                commentAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.thumbsUpImg:
                //点赞
                pointThumbsUp(Domain.getArticleResponse().getArticleId(),Domain.getUserId());
                break;
            case R.id.thumbsDownImg:
                //点踩
                thumbsDown(Domain.getArticleResponse().getArticleId(),Domain.getUserId());
                break;
            case R.id.commentImg:
                //评论
                commentLinear.setVisibility(1);
                //rv.setVisibility(-1);
                break;
            case R.id.publicComment:
                //发表评论
                String comment=commentEdit.getText().toString();
                if("".equals(comment)){
                    Toast.makeText(DetailArticle.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                }
                else{
                    publicComment(Domain.getUserId(),Domain.getArticleResponse().getArticleId(),comment);
                }
                break;
            case R.id.imageShow:
                Intent otherInfoIntent=new Intent();
                otherInfoIntent.setClass(DetailArticle.this,UserInfo.class);
                DetailArticle.this.startActivity(otherInfoIntent);
                break;
            default:
                break;
        }
    }
    public void pointThumbsUp(int articleId,int userId){
        JSONObject resquestMsg=new JSONObject();
        try {
            resquestMsg.put("articleId",articleId);
            resquestMsg.put("userId",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUnit.postHttpRequest(resquestMsg, thumbsUpAddress, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject responseMsg=new JSONObject(response);
                String result=responseMsg.getString("data");
                Message msg=new Message();
                msg.obj=result+"ThumbsUp";
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                Log.e("DetailArticle.java","connectError");
            }
        });
    }
    public void thumbsDown(int articleId,int userId){
        JSONObject requestMsg=new JSONObject();
        try {
            requestMsg.put("articleId",articleId);
            requestMsg.put("userId",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUnit.postHttpRequest(requestMsg, thumbsDownAddress, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject responseMsg=new JSONObject(response);
                String result=responseMsg.getString("data");
                Message msg=new Message();
                msg.obj=result+"ThumbsDown";
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                Log.e("DetailArticle.java","connectError");
            }
        });
    }
    public void publicComment(int userId,int articleId,String articleComment){
        JSONObject resquestMsg=new JSONObject();
        try {
            resquestMsg.put("userId",userId);
            resquestMsg.put("articleId",articleId);
            resquestMsg.put("articleComment",articleComment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUnit.postHttpRequest(resquestMsg, publishCommentAddress, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject responseMsg=new JSONObject(response);
                String result=responseMsg.getString("data");
                Message msg=new Message();
                msg.obj=result+"PublicComment";
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                Log.e("DetailArticle.class","connectError");
            }
        });
    }
}
