package com.xoptimal.revhelper.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xoptimal.rcvhelper.entity.FDLoadMore;
import com.xoptimal.rcvhelper.view.INetViewGroup;

import me.drakeet.multitype.ItemViewBinder;


/**
 * Created by Freddie on 2018/2/25 0025 .
 * Description:
 */
public class NetViewHolder extends ItemViewBinder<FDLoadMore, RecyclerView.ViewHolder> {

    private INetViewGroup mViewGroup;

    public NetViewHolder(INetViewGroup viewGroup) {
        mViewGroup = viewGroup;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new RecyclerView.ViewHolder(new FrameLayout(inflater.getContext())) {
        };
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @NonNull FDLoadMore item) {
        View    view    = null;
        switch (item.getStatus()) {
            case NORMAL:
                view = mViewGroup.initNormal();
                break;
            case LOADING:
                view = mViewGroup.initLoadingView();
                break;
            case ERROR:
                view = mViewGroup.initErrorView(new Throwable(item.getMessage()));
                break;
            case MOREOVER:
                view = mViewGroup.initMoreover();
                break;
            case EMPTY:
                view = mViewGroup.initEmptyView();
                break;
        }
        FrameLayout parent = ((FrameLayout) holder.itemView);
        parent.removeAllViews();
        parent.addView(view);
    }


}
