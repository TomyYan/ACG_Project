package com.example.tomy.acg_project.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tomy.acg_project.R;
import com.example.tomy.acg_project.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomy on 18-3-5.
 */
public class AFragment extends Fragment {

    //private RecyclerView mRecyclerView;

    //private RecyclerView.Adapter mAdapter;

    //private RecyclerView.LayoutManager mLayoutManager;

    private View view;
    private ArrayList<String> mDatas;
    private MyAdapter adapter;

    public AFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatas = new ArrayList<>();
        for (int i=0;i<10;i++){
            mDatas.add(""+i);
        }
        view = inflater.inflate(R.layout.fragment_a, container,false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.a_recycler_view);
        //设置recyclerview的布局样式
        rv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        //将布局设置为表格布局，3为3列
        // rv.setLayoutManager(new GridLayoutManager(getContext(),3,GridLayoutManager.VERTICAL,false));
        //设置recyclerview的分隔线 DividerItemDecoration是一个独立的文件
        //rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        adapter = new MyAdapter(mDatas);
        rv.setAdapter(adapter);
        return view;
    }
}
