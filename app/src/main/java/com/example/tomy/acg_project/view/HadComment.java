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
import android.widget.TextView;
import com.example.tomy.acg_project.R;
import com.example.tomy.acg_project.UseTool.GetArticle;
import com.example.tomy.acg_project.UseTool.ShowDeleteWindows;
import com.example.tomy.acg_project.adapter.MyAdapter;
import com.example.tomy.acg_project.domain.ArticleResponse;
import com.example.tomy.acg_project.domain.Domain;
import com.example.tomy.acg_project.domain.EndLessOnScrollListener;

import java.util.ArrayList;

/**
 * Created by tomy on 18-5-9.
 */
public class HadComment extends AppCompatActivity {

    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView titleShow;
    private View view;
    private static MyAdapter adapter;
    private static int start=0;
    private static Activity activity;
    public static ArrayList<ArticleResponse> article=new ArrayList<>();
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.have_do_article);

        titleShow=(TextView)findViewById(R.id.titleShow);
        titleShow.setText("已评论");

        activity=this;

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.me_swipe_refresh_layout);
        rv = (RecyclerView)findViewById(R.id.me_recycler_view);
        linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        //设置recyclerview的布局样式
        rv.setLayoutManager(linearLayoutManager);
        //设置recyclerview的分隔线 DividerItemDecoration是一个独立的文件
        //rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        adapter = new MyAdapter(article);
        rv.setAdapter(adapter);
        //recyclerView滚动监听
        //rv.addOnScrollListener(new MyRecyclerViewScrollListener());

        //获取数据进行填充
        new GetArticle().getCommentArticle(Domain.getUserId(),start);
        //设置Item点击监听
        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                //Toast.makeText(activity,"您点击了"+rv.getChildAdapterPosition(view)+"行",Toast.LENGTH_SHORT).show();
                //跳转到文章详细显示页面
                Intent intent=new Intent();
                intent.setClass(HadComment.this,DetailArticle.class);
                System.out.println("文章Id为:"+view.getTag().toString());
                Domain.setArticleResponse(article.get(rv.getChildAdapterPosition(view)));
                HadComment.this.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view) {
                //Toast.makeText(activity,"您长点击了"+rv.getChildAdapterPosition(view)+"行",Toast.LENGTH_SHORT).show();
                //弹出弹窗询问是否确定删除帖子界面
                //记录点击信息
                //Domain.setArticleResponse(article.get(rv.getChildAdapterPosition(view)));
                //显示对话框
                //new ShowDeleteWindows(HadComment.this).showIfDelete();
            }

        });
        //上拉加载更多
        rv.addOnScrollListener(new EndLessOnScrollListener(linearLayoutManager) {
            @Override public void onLoadMore(int currentPage) {
                Log.e("fragment.class","上拉"+start);
                //加载数据
                new GetArticle().getCommentArticle(Domain.getUserId(),start);
            }
        });

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            public void onRefresh() {
                article=new ArrayList<>();
                adapter.setmData(article);
                start=0;
                new GetArticle().getCommentArticle(Domain.getUserId(),start);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
        //adapter.notifyDataSetChanged();
    }
}
