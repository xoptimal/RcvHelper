package com.xoptimal.rcvhelper.view;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xoptimal.rcvhelper.R;

/**
 * Created by Freddie on 2018/4/4 0004 .
 * Description:
 */
public class ImplNetViewGroup extends INetViewGroup {

    public ImplNetViewGroup(Context context, ViewGroup root) {
        super(context, root);
    }

    @Override
    public View initNormal() {
        return LayoutInflater.from(mContext).inflate(R.layout.fd_view_normal, mRootView, false);
    }

    @Override
    public View initEmptyView() {
        return LayoutInflater.from(mContext).inflate(R.layout.fd_view_empty, mRootView, false);
    }

    @Override
    public View initErrorView(Throwable e) {
        return LayoutInflater.from(mContext).inflate(R.layout.fd_view_error, mRootView, false);
    }

    @Override
    public View initLoadingView() {
        return LayoutInflater.from(mContext).inflate(R.layout.fd_view_loading, mRootView, false);
    }

    @Override
    public View initMoreover() {
        return LayoutInflater.from(mContext).inflate(R.layout.fd_view_moreover, mRootView, false);
    }
}
