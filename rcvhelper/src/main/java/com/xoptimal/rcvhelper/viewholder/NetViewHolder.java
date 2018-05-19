package com.xoptimal.rcvhelper.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xoptimal.rcvhelper.R;
import com.xoptimal.rcvhelper.entity.NetStatus;
import com.xoptimal.rcvhelper.listener.OnNetListener;

import me.drakeet.multitype.ItemViewBinder;


/**
 * Created by Freddie on 2018/2/25 0025 .
 * Description:
 */
public class NetViewHolder extends ItemViewBinder<NetStatus, RecyclerView.ViewHolder> {

    private OnNetListener mOnNetListener;

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new RecyclerView.ViewHolder(new FrameLayout(inflater.getContext())) {
        };
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @NonNull NetStatus item) {

        FrameLayout rootLayout = (FrameLayout) holder.itemView;
        Context     context    = rootLayout.getContext();

        if (rootLayout.getChildCount() > 0) rootLayout.removeAllViews();

        View view = null;

        switch (item.getStatus()) {
            case NORMAL:
                view = LayoutInflater.from(context).inflate(R.layout.fd_view_normal, rootLayout, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnNetListener != null) mOnNetListener.onLoadMore();
                    }
                });
                break;
            case LOADING:
                view = LayoutInflater.from(context).inflate(R.layout.fd_view_loading, rootLayout, false);
                break;
            case ERROR:
                view = LayoutInflater.from(context).inflate(R.layout.fd_view_error, rootLayout, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnNetListener != null) mOnNetListener.onLoadMore();
                    }
                });
                break;
            case MOREOVER:
                view = LayoutInflater.from(context).inflate(R.layout.fd_view_moreover, rootLayout, false);
                break;
            case EMPTY:
                view = LayoutInflater.from(context).inflate(R.layout.fd_view_empty, rootLayout, false);
                view.findViewById(R.id.iv_icon).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnNetListener != null) mOnNetListener.onRefresh();
                    }
                });
                view.findViewById(R.id.tv_value).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnNetListener != null) mOnNetListener.onRefresh();
                    }
                });
                break;
        }

        rootLayout.addView(view);
        rootLayout.setLayoutParams(view.getLayoutParams());
    }

    public void setOnNetListener(OnNetListener onNetListener) {
        mOnNetListener = onNetListener;
    }
}
