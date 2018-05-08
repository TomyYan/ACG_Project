package com.example.tomy.acg_project.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.Toast;
import com.example.tomy.acg_project.R;
import com.example.tomy.acg_project.UseTool.GetArticle;
import com.example.tomy.acg_project.UseTool.ShowDeleteWindows;
import com.example.tomy.acg_project.adapter.MyAdapter;
import com.example.tomy.acg_project.domain.*;
import com.example.tomy.acg_project.view.DetailArticle;
import com.example.tomy.acg_project.view.MainActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tomy on 18-3-5.
 */
public class AFragment extends Fragment {

    //private RecyclerView mRecyclerView;

    //private RecyclerView.Adapter mAdapter;

    //private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String DeleteArticleAdress=Domain.Server_Address+"deleteArticle";

    private View view;
    private static MyAdapter adapter;
    private static int start=0;
    private static Activity activity;

    public static ArrayList<ArticleResponse> article=new ArrayList<>();

    private RecyclerView rv;

//    private Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.obj.toString()){
//                case "ok":
//                    Toast.makeText(activity,"删除成功",Toast.LENGTH_SHORT).show();
//                    break;
//                case "fail":
//                    Toast.makeText(activity,"删除失败",Toast.LENGTH_SHORT).show();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    public AFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*
        mDatas = new ArrayList<>();
        for (int i=0;i<10;i++){
            mDatas.add(""+i);
        }
        */

        activity=getActivity();
        view = inflater.inflate(R.layout.fragment_a, container,false);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        rv = (RecyclerView) view.findViewById(R.id.a_recycler_view);

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
        new GetArticle().getArticle(1,start);
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
                Log.e("fragment.class","上拉"+start);
                //加载数据
                new GetArticle().getArticle(1,start);
            }
        });

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            public void onRefresh() {
                article=new ArrayList<>();
                adapter.setmData(article);
                start=0;
                new GetArticle().getArticle(1,start);
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

//    public void showIfDelete(){
//        //显示删除的缩略信息
//        String showMsg="";
//        if(Domain.getArticleResponse().getArticle().length()>5){
//            showMsg="是否删除"+"\""+Domain.getArticleResponse().getArticle().substring(0,5)+"...\"";
//        }
//        else{
//            showMsg="是否删除"+"\""+Domain.getArticleResponse().getArticle()+"...\"";
//        }
//        // 这里的属性可以一直设置，因为每次设置后返回的是一个builder对象
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        // 设置提示框的标题
//        builder.setTitle("警告").
//                // 设置提示框的图标
//                //setIcon(R.drawable.ic_launcher).
//                // 设置要显示的信息
//                setMessage(showMsg).
//                // 设置确定按钮
//                setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //删除文章
//                        final JSONObject requestMsg=new JSONObject();
//                        try {
//                            requestMsg.put("articleId",Domain.getArticleResponse().getArticleId());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        HttpUnit.postHttpRequest(requestMsg, DeleteArticleAdress, new HttpCallbackListener() {
//                            @Override
//                            public void onFinish(String response) throws JSONException {
//                                JSONObject responseMsg=new JSONObject(response);
//                                Message msg=new Message();
//                                msg.obj=responseMsg.get("data");
//                                handler.sendMessage(msg);
//                            }
//                            @Override
//                            public void onError(Exception e) {
//                                Log.e("deleteArticle","connectError");
//                            }
//                        });
//                    }
//                }).
//                // 设置取消按钮,null是什么都不做
//                setNegativeButton("取消", null);
//        // 生产对话框
//        AlertDialog alertDialog = builder.create();
//        // 显示对话框
//        alertDialog.show();
//    }

}
