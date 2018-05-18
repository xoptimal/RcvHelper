package com.xoptimal.rcvhelper.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xoptimal.rcvhelper.entity.NetStatus;

import me.drakeet.multitype.ItemViewBinder;


/**
 * Created by Freddie on 2018/2/25 0025 .
 * Description:
 */
public class NetViewHolder extends ItemViewBinder<NetStatus, RecyclerView.ViewHolder> {

    private int layoutId;

    public NetViewHolder(int layoutId) {
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new RecyclerView.ViewHolder(inflater.inflate(layoutId, parent, false)) {
        };
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @NonNull NetStatus item) {

    }


}
