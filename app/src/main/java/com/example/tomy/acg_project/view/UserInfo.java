package com.example.tomy.acg_project.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.example.tomy.acg_project.R;
import com.example.tomy.acg_project.UseTool.GetArticle;
import com.example.tomy.acg_project.UseTool.ShowDeleteWindows;
import com.example.tomy.acg_project.adapter.MyAdapter;
import com.example.tomy.acg_project.domain.ArticleResponse;
import com.example.tomy.acg_project.domain.Domain;
import com.example.tomy.acg_project.domain.EndLessOnScrollListener;

import java.util.ArrayList;

/**
 * Created by tomy on 18-5-6.
 */
public class UserInfo extends AppCompatActivity implements View.OnClickListener{

    private EditText othersAccountInput,othersNickNameInput,othersSexInput,othersEmailInput,othersSignInput;
    private ImageButton imgButton;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static MyAdapter adapter;
    private static int start=0;
    private RecyclerView rv;
    private RelativeLayout me_top;
    public static ArrayList<ArticleResponse> article=new ArrayList<>();
    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        activity=this;
        //初始化数据
        article=new ArrayList<>();
        //获取界面组件
        othersAccountInput=(EditText)findViewById(R.id.othersAccountInput);
        othersNickNameInput=(EditText)findViewById(R.id.othersNickNameInput);
        othersSexInput=(EditText)findViewById(R.id.othersSexInput);
        othersEmailInput=(EditText)findViewById(R.id.othersEmailInput);
        othersSignInput=(EditText)findViewById(R.id.othersSignInput);
        imgButton=(ImageButton)findViewById(R.id.imgButton);
        //初始化界面
        initUserInfo();

        //设置头像
        imgButton.setImageBitmap(Domain.othersImg);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.me_swipe_refresh_layout);
        rv = (RecyclerView)findViewById(R.id.me_recycler_view);
        linearLayoutManager=new LinearLayoutManager(UserInfo.this,LinearLayoutManager.VERTICAL,false);
        //设置recyclerview的布局样式
        rv.setLayoutManager(linearLayoutManager);
        //设置recyclerview的分隔线 DividerItemDecoration是一个独立的文件
        //rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        adapter = new MyAdapter(article);
        rv.setAdapter(adapter);
        //recyclerView滚动监听
        //rv.addOnScrollListener(new MyRecyclerViewScrollListener());

        //获取数据进行填充
        start=0;
        adapter.setmData(article);
        new GetArticle().getUserArticle(Domain.getArticleResponse().getUserId(),start);
        //设置Item点击监听
        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                //Toast.makeText(activity,"您点击了"+rv.getChildAdapterPosition(view)+"行",Toast.LENGTH_SHORT).show();
                //跳转到文章详细显示页面
                Intent intent=new Intent();
                intent.setClass(UserInfo.this,DetailArticle.class);
                System.out.println("文章Id为:"+view.getTag().toString());
                Domain.setArticleResponse(article.get(rv.getChildAdapterPosition(view)));
                UserInfo.this.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view) {
                //Toast.makeText(activity,"您长点击了"+rv.getChildAdapterPosition(view)+"行",Toast.LENGTH_SHORT).show();
                //弹出弹窗询问是否确定删除帖子界面
                //记录点击信息
//                Domain.setArticleResponse(article.get(rv.getChildAdapterPosition(view)));
//                //显示对话框
//                new ShowDeleteWindows(activity).showIfDelete();
            }

        });
        //上拉加载更多
        rv.addOnScrollListener(new EndLessOnScrollListener(linearLayoutManager) {
            @Override public void onLoadMore(int currentPage) {
                Log.e("fragment.class","上拉"+start);
                //加载数据
                new GetArticle().getUserArticle(Domain.getArticleResponse().getUserId(),start);
            }
        });

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            public void onRefresh() {
                article=new ArrayList<>();
                adapter.setmData(article);
                start=0;
                new GetArticle().getUserArticle(Domain.getArticleResponse().getUserId(),start);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    //设置用户信息
    public void initUserInfo(){
        othersAccountInput.setText(Domain.getOtherInfo().getAccount());
        othersNickNameInput.setText(Domain.getOtherInfo().getUserName());
        othersSexInput.setText(Domain.getOtherInfo().getAccountSex());
        othersEmailInput.setText(Domain.getOtherInfo().getEmail());
        othersSignInput.setText(Domain.getOtherInfo().getAccountSign());
    }

        //更新recyclerView
    public static void updateView(ArticleResponse articleResponse){
        start++;
        article.add(articleResponse);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
