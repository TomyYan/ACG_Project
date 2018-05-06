package com.example.tomy.acg_project.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.tomy.acg_project.R;
import com.example.tomy.acg_project.domain.ArticleResponse;

import java.util.ArrayList;

/**
 * Created by tomy on 18-3-27.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{



    private ArrayList<ArticleResponse> mData;

    public MyAdapter(ArrayList<ArticleResponse> data) {
        this.mData = data;
    }

    public void updateData(ArrayList<ArticleResponse> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    private OnItemClickListener mItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view);
        void onItemLongClick(View view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this. mItemClickListener=onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_a_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mItemClickListener.onItemClick(v); } });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                mItemClickListener.onItemLongClick(v);
                return true; } });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 绑定数据
        holder.authorName.setText(mData.get(position).getUserName());
        holder.article.setText(mData.get(position).getArticle());
        holder.thumbsUpNum.setText(mData.get(position).getThumbsUpNum()+"");
        holder.thumbsDownNum.setText(mData.get(position).getThumbsDownNum()+"");
        //将Item绑定articleId
        holder.itemView.setTag(mData.get(position).getArticleId());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton imageShow;
        TextView authorName,article,thumbsUpNum,thumbsDownNum;

        //TextView mTv;

        public ViewHolder(View itemView) {
            super(itemView);
            imageShow=(ImageButton)itemView.findViewById(R.id.imageShow);
            authorName=(TextView)itemView.findViewById(R.id.authorName);
            article=(TextView)itemView.findViewById(R.id.article);
            thumbsUpNum=(TextView)itemView.findViewById(R.id.thumbsUpNum);
            thumbsDownNum=(TextView)itemView.findViewById(R.id.thumbsDownNum);
            //mTv = (TextView) itemView.findViewById(R.id.ViewOfAuthor);
        }
    }
    public ArrayList<ArticleResponse> getmData() {
        return mData;
    }

    public void setmData(ArrayList<ArticleResponse> mData) {
        this.mData = mData;
    }
}
