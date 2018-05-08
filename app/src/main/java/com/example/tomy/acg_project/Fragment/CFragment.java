package com.example.tomy.acg_project.Fragment;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;
import com.example.tomy.acg_project.R;
import com.example.tomy.acg_project.UseTool.GetArticle;
import com.example.tomy.acg_project.UseTool.ShowDeleteWindows;
import com.example.tomy.acg_project.adapter.MyAdapter;
import com.example.tomy.acg_project.domain.ArticleResponse;
import com.example.tomy.acg_project.domain.Domain;
import com.example.tomy.acg_project.domain.EndLessOnScrollListener;
import com.example.tomy.acg_project.view.DetailArticle;

import java.util.ArrayList;

/**
 * Created by tomy on 18-3-5.
 */
public class CFragment extends Fragment {
    public CFragment(){
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_c, container, false);
//    }
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;


    private View view;
    private static MyAdapter adapter;
    private static int start=0;
    private static Activity activity;

    public static ArrayList<ArticleResponse> article=new ArrayList<>();

    private RecyclerView rv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity=getActivity();
        view = inflater.inflate(R.layout.fragment_c, container,false);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.c_swipe_refresh_layout);
        rv = (RecyclerView) view.findViewById(R.id.c_recycler_view);

        linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        //设置recyclerview的布局样式
        rv.setLayoutManager(linearLayoutManager);
        //将布局设置为表格布局，3为3列
        // rv.setLayoutManager(new GridLayoutManager(getContext(),3,GridLayoutManager.VERTICAL,false));
        //设置recyclerview的分隔线 DividerItemDecoration是一个独立的文件
        //rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        adapter = new MyAdapter(article);
        rv.setAdapter(adapter);
        //获取数据进行填充
        new GetArticle().getArticle(2,start);
        //设置Item点击监听
        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                //Toast.makeText(activity,"您点击了"+rv.getChildAdapterPosition(view)+"行",Toast.LENGTH_SHORT).show();
                //跳转到文章详细显示页面
                //Intent intent=new Intent(activity, DetailArticle.class);
                Intent intent=new Intent();
                intent.setClass(activity,DetailArticle.class);
                //System.out.println("获得点击位置为:"+rv.getChildAdapterPosition(view));
                //int a=rv.getChildAdapterPosition(view);
                //intent.putExtra("position",a);
                System.out.println("文章Id为:"+view.getTag().toString());
                Domain.setArticleResponse(article.get(rv.getChildAdapterPosition(view)));
                activity.startActivity(intent);

            }

            @Override
            public void onItemLongClick(View view) {
                //Toast.makeText(activity,"您长点击了"+rv.getChildAdapterPosition(view)+"行",Toast.LENGTH_SHORT).show();
                if(Domain.getUserInfo().getIsAdmin()==1) {
                    //记录点击信息
                    Domain.setArticleResponse(article.get(rv.getChildAdapterPosition(view)));
                    //显示对话框
                    new ShowDeleteWindows(activity).showIfDelete();
                }
            }

        });
        //上拉加载更多
        rv.addOnScrollListener(new EndLessOnScrollListener(linearLayoutManager) {
            @Override public void onLoadMore(int currentPage) {
                //loadMoreData();
                Log.e("fragment.class","上拉"+start);

                new GetArticle().getArticle(2,start);
            }
        });

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            public void onRefresh() {
                //我在List最前面加入一条数据
                // mData.add(0, "嘿，我是“下拉刷新”生出来的");
                // 数据重新加载完成后，提示数据发生改变，并且设置现在不在刷新
                // mAdapter.notifyDataSetChanged();
                // mRefreshLayout.setRefreshing(false);
                article=new ArrayList<>();
                adapter.setmData(article);
                start=0;
                new GetArticle().getArticle(2,start);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return view;
    }

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
}
