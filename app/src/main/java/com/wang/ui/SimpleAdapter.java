package com.wang.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wang.R;

import java.util.List;

/**
 * Created by 28724 on 2018/4/11.
 */

public class SimpleAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<String> mDatas;

    public interface OnItemClickLisener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    private OnItemClickLisener mOnItemClickLisener;

    public void setOnItemClickLisener(OnItemClickLisener listener)
    {
        this.mOnItemClickLisener=listener;
    }
    public SimpleAdapter(Context context, List<String> datas) {
        this.mContext=context;
        this.mDatas=datas;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {

        View view=mInflater.inflate(R.layout.item_text,arg0,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {
        holder.tv.setText(mDatas.get(pos));
        if(mOnItemClickLisener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int layoutPosition=holder.getLayoutPosition();
                    mOnItemClickLisener.onItemClick(holder.itemView,layoutPosition);

                }
            });

            //longclick
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int layoutPosition=holder.getLayoutPosition();
                    mOnItemClickLisener.onItemLongClick(holder.itemView,layoutPosition);
                    return false;
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addData(int pos)
    {
        mDatas.add(pos,"Insert One");
        notifyItemInserted(pos);
    }

    public void deleteData(int pos)
    {
        mDatas.remove(pos);
        notifyItemRemoved(pos);

    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    TextView tv;
    public MyViewHolder(View arg0) {
        super(arg0);
        tv=arg0.findViewById(R.id.text_view);
    }


}
