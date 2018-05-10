package com.xoptimal.revhelper.view;

import android.content.Context;
import android.view.View;

/**
 * Created by Freddie on 2017/9/6 0006 .
 * Description:
 */
public abstract class INetViewGroup {

    protected Context       mContext;
    protected OnNetListener mNetListener;

    INetViewGroup(Context context) {
        mContext = context;
    }

    public void setNetListener(OnNetListener netListener) {
        mNetListener = netListener;
    }

    public abstract View initNormal();

    public abstract View initEmptyView();

    public abstract View initErrorView(Throwable e);

    public abstract View initLoadingView();

    public abstract View initMoreover();

    public interface OnNetListener {
        void onRefresh();

        void onLoadMore();
    }
}

