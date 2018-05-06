package com.example.tomy.acg_project.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.tomy.acg_project.R;
import com.example.tomy.acg_project.domain.ArticleResponse;
import com.example.tomy.acg_project.domain.CommentResponse;

import java.util.ArrayList;

/**
 * Created by tomy on 18-4-15.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<CommentResponse> mData;

    public CommentAdapter (ArrayList<CommentResponse> data) {
        this.mData = data;
    }

    public void updateData(CommentResponse comment) {
        mData.add(comment);
        //notifyDataSetChanged();
    }

    /*
    private MyAdapter.OnItemClickListener mItemClickListener;
    public static interface OnItemClickListener {
        void onItemClick(View view);
        void onItemLongClick(View view);
    }

    public void setOnItemClickListener(MyAdapter.OnItemClickListener onItemClickListener ){
        this. mItemClickListener=onItemClickListener;
    }
    */
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        /*
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mItemClickListener.onItemClick(v); } });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                mItemClickListener.onItemLongClick(v);
                return true; } });
        */
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        // 绑定数据
        holder.articleComment.setText(mData.get(position).getArticleComment());
        holder.userName.setText(mData.get(position).getUserName());
//        holder.authorName.setText(mData.get(position).getUserName());
//        holder.article.setText(mData.get(position).getArticle());
//        holder.thumbsUpNum.setText(mData.get(position).getThumbsUpNum()+"");
//        holder.thumbsDownNum.setText(mData.get(position).getThumbsDownNum()+"");
        //将Item绑定articleId
        //holder.itemView.setTag(mData.get(position).getArticleId());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton accountImg;
        TextView userName,articleComment;
//        ImageButton imageShow;
//        TextView authorName,article,thumbsUpNum,thumbsDownNum;

        //TextView mTv;

        public ViewHolder(View itemView) {
            super(itemView);
            accountImg=(ImageButton)itemView.findViewById(R.id.accountImg);
            userName=(TextView)itemView.findViewById(R.id.userName);
            articleComment=(TextView)itemView.findViewById(R.id.articleComment);
//            imageShow=(ImageButton)itemView.findViewById(R.id.imageShow);
//            authorName=(TextView)itemView.findViewById(R.id.authorName);
//            article=(TextView)itemView.findViewById(R.id.article);
//            thumbsUpNum=(TextView)itemView.findViewById(R.id.thumbsUpNum);
//            thumbsDownNum=(TextView)itemView.findViewById(R.id.thumbsDownNum);
            //mTv = (TextView) itemView.findViewById(R.id.ViewOfAuthor);
        }
    }
}
