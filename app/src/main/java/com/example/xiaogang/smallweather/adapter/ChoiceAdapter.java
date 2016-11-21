package com.example.xiaogang.smallweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xiaogang.smallweather.R;

import java.util.ArrayList;

/**
 * Created by xiaogang on 16/11/15.
 */

public class ChoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private Context mContext;
    private ArrayList<String> mdataList;

    public ChoiceAdapter(Context mContext, ArrayList<String> mdataList) {
        this.mContext = mContext;
        this.mdataList = mdataList;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view);
        }
    }
//    临时笔记，这次实现adapter点击事件忘记implements View.OnClickListener和onCreateViewHolder里的view.setOnClickListener(this);
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);

    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(mContext).inflate(R.layout.item_city, parent, false);
        view.setOnClickListener(this);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CityViewHolder){
            ((CityViewHolder) holder).textView.setText(mdataList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mdataList.size();
    }

    public class CityViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public CityViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.cityname);
        }
    }
}
