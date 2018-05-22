package com.example.tomy.acg_project.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.*;
import com.example.tomy.acg_project.R;
import com.example.tomy.acg_project.UseTool.GetArticle;
import com.example.tomy.acg_project.UseTool.ShowDeleteWindows;
import com.example.tomy.acg_project.adapter.MyAdapter;
import com.example.tomy.acg_project.domain.ArticleResponse;
import com.example.tomy.acg_project.domain.Domain;
import com.example.tomy.acg_project.domain.EndLessOnScrollListener;
import com.example.tomy.acg_project.domain.updataPhoto;
import com.example.tomy.acg_project.view.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by tomy on 18-3-5.
 */
public class MeFragment extends Fragment implements View.OnClickListener,updataPhoto {
    public MeFragment() {
    }

    private AlphaAnimation mShowAnim, mHiddenAmin;//控件的显示和隐藏动画


    //监听器
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.changeInfo:
                //跳转到修改个人信息页面
                Intent changeViewIntent=new Intent();
                changeViewIntent.setClass(getActivity(), ChangeUserInfo.class);
                getActivity().startActivity(changeViewIntent);
                break;
            case R.id.changePassword:
                //跳转到修改密码页面
                Intent changePasswordViewIntent=new Intent();
                changePasswordViewIntent.setClass(getActivity(),ChangePassword.class);
                getActivity().startActivity(changePasswordViewIntent);
                break;
            case R.id.hadPublic:
                Intent hadPublicIntent=new Intent();
                hadPublicIntent.setClass(getActivity(), HadPublic.class);
                getActivity().startActivity(hadPublicIntent);
                break;
            case R.id.hadComment:
                Intent hadCommentIntent=new Intent();
                hadCommentIntent.setClass(getActivity(), HadComment.class);
                getActivity().startActivity(hadCommentIntent);
                break;
            case R.id.setting:
                Intent settingIntent=new Intent();
                settingIntent.setClass(getActivity(),Setting.class);
                Domain.setMainActivity(getActivity());
                getActivity().startActivity(settingIntent);
                break;
            default:
                break;
        }
    }

    //成员变量
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    private static MyAdapter adapter;
    private static int start=0;
    private static Activity activity;
    public static ArrayList<ArticleResponse> article=new ArrayList<>();
    private RecyclerView rv;
    private EditText accountInput,nickNameInput,sexInput,meEmailInput,meSignInput;
    private ImageButton imgButton;
    private RelativeLayout me_top;
    private Button changeInfo,changePassword,hadPublic,hadComment,setting;
    private Bitmap img;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //获取主线程活动
        activity=getActivity();
        view = inflater.inflate(R.layout.fragment_me, container,false);

        Domain.updata=this;

        //控件显示的动画
        mShowAnim = new AlphaAnimation(0.0f, 1.0f);
        mShowAnim.setDuration(300);

        //控件隐藏的动画
        mHiddenAmin = new AlphaAnimation(1.0f, 0.0f);
        mHiddenAmin.setDuration(300);

        //获取界面组件
        me_top=(RelativeLayout)view.findViewById(R.id.me_top);
        accountInput=(EditText)view.findViewById(R.id.accountInput);
        nickNameInput=(EditText)view.findViewById(R.id.nickNameInput);
        sexInput=(EditText)view.findViewById(R.id.sexInput);
        meEmailInput=(EditText)view.findViewById(R.id.meEmailInput);
        meSignInput=(EditText)view.findViewById(R.id.meSignInput);
        imgButton=(ImageButton)view.findViewById(R.id.imgButton);
        changeInfo=(Button)view.findViewById(R.id.changeInfo);
        changePassword=(Button)view.findViewById(R.id.changePassword);
        hadPublic=(Button)view.findViewById(R.id.hadPublic);
        hadComment=(Button)view.findViewById(R.id.hadComment);
        setting=(Button)view.findViewById(R.id.setting);
        //设置按钮监听
        changeInfo.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        hadPublic.setOnClickListener(this);
        hadComment.setOnClickListener(this);
        setting.setOnClickListener(this);
        //初始化界面
        initUserInfo();
        if(Domain.img!=null){
            imgButton.setImageBitmap(Domain.img);
        }


//        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.me_swipe_refresh_layout);
//        rv = (RecyclerView) view.findViewById(R.id.me_recycler_view);
//        linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
//        //设置recyclerview的布局样式
//        rv.setLayoutManager(linearLayoutManager);
//        //设置recyclerview的分隔线 DividerItemDecoration是一个独立的文件
//        //rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
//        adapter = new MyAdapter(article);
//        rv.setAdapter(adapter);
//        //recyclerView滚动监听
//        //rv.addOnScrollListener(new MyRecyclerViewScrollListener());
//
//        //获取数据进行填充
//        new GetArticle().getUserArticle(Domain.getUserId(),start);
//        //设置Item点击监听
//        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view) {
//                //Toast.makeText(activity,"您点击了"+rv.getChildAdapterPosition(view)+"行",Toast.LENGTH_SHORT).show();
//                //跳转到文章详细显示页面
//                Intent intent=new Intent();
//                intent.setClass(activity,DetailArticle.class);
//                System.out.println("文章Id为:"+view.getTag().toString());
//                Domain.setArticleResponse(article.get(rv.getChildAdapterPosition(view)));
//                activity.startActivity(intent);
//            }
//
//            @Override
//            public void onItemLongClick(View view) {
//                //Toast.makeText(activity,"您长点击了"+rv.getChildAdapterPosition(view)+"行",Toast.LENGTH_SHORT).show();
//                //弹出弹窗询问是否确定删除帖子界面
//                //记录点击信息
//                Domain.setArticleResponse(article.get(rv.getChildAdapterPosition(view)));
//                //显示对话框
//                new ShowDeleteWindows(activity).showIfDelete();
//            }
//
//        });
//        //上拉加载更多
//        rv.addOnScrollListener(new EndLessOnScrollListener(linearLayoutManager) {
//            @Override public void onLoadMore(int currentPage) {
//                Log.e("fragment.class","上拉"+start);
//                //加载数据
//                new GetArticle().getUserArticle(Domain.getUserId(),start);
//            }
//        });
//
//        //下拉刷新
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
//            public void onRefresh() {
//                article=new ArrayList<>();
//                adapter.setmData(article);
//                start=0;
//                new GetArticle().getUserArticle(Domain.getUserId(),start);
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
        return view;
    }

//    //更新recyclerView
//    public static void updateView(ArticleResponse articleResponse){
//        start++;
//        article.add(articleResponse);
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                adapter.notifyDataSetChanged();
//            }
//        });
//    }


    //设置用户信息
    public void initUserInfo(){
        accountInput.setText(Domain.getUserInfo().getAccount());
        nickNameInput.setText(Domain.getUserInfo().getUserName());
        sexInput.setText(Domain.getUserInfo().getAccountSex());
        meEmailInput.setText(Domain.getUserInfo().getEmail());
        meSignInput.setText(Domain.getUserInfo().getAccountSign());
    }

    @Override
    public void updataPhoto() {
        if(Domain.updata!=null){
            try{
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(Domain.img!=null){
                            imgButton.setImageBitmap(Domain.img);
                        }
                        initUserInfo();
                    }
                });
            }catch (Exception e){

            }
        }
    }
}
