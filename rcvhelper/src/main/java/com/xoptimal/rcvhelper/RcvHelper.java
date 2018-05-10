package com.xoptimal.rcvhelper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.xoptimal.rcvhelper.entity.FDLoadMore;
import com.xoptimal.rcvhelper.listener.OnRcvScrollListener;
import com.xoptimal.rcvhelper.provider.NetViewHolder;
import com.xoptimal.rcvhelper.view.INetViewGroup;
import com.xoptimal.rcvhelper.view.ImplNetViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by Freddie on 2016/03/12 .
 * Description:
 */
public class RcvHelper {

    private RecyclerView mRecyclerView;
    private MultiTypeAdapter mAdapter;
    private boolean mLoadMore;
    private boolean hasLoadMore;
    private boolean mSmartLoadMoreView;
    private boolean mAutoLoadMore;
    private Items mItems;
    private INetViewGroup mNetViewGroup;

    protected RcvHelper(RecyclerView recyclerView, INetViewGroup viewGroup, boolean loadMore, boolean autoLoadMore, boolean smartLoadMoreView) {
        mLoadMore = loadMore;
        mAutoLoadMore = autoLoadMore;
        mSmartLoadMoreView = smartLoadMoreView;
        mRecyclerView = recyclerView;
        mAdapter = new MultiTypeAdapter();
        mAdapter.setItems(mItems = new Items());
        mAdapter.register(FDLoadMore.class, new NetViewHolder(mNetViewGroup = viewGroup));
        if (mRecyclerView.getLayoutManager() == null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        }
        mRecyclerView.setAdapter(mAdapter);
        resetItemView();
        if (mLoadMore) initEvent();
    }

    public static class Builder {

        private RecyclerView mRecyclerView;
        private INetViewGroup mViewGroup;

        private boolean mLoadMore = true;
        private boolean mSmartLoadMoreView = true;
        private boolean mAutoLoadMore = true;

        private INetViewGroup.OnNetListener mNetListener;

        public Builder(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
        }

        public Builder setViewGroup(INetViewGroup viewGroup) {
            mViewGroup = viewGroup;
            return this;
        }

        public Builder setmLoadMore(boolean mLoadMore) {
            this.mLoadMore = mLoadMore;
            return this;
        }

        public Builder setmSmartLoadMoreView(boolean mSmartLoadMoreView) {
            this.mSmartLoadMoreView = mSmartLoadMoreView;
            return this;
        }

        public Builder setmAutoLoadMore(boolean mAutoLoadMore) {
            this.mAutoLoadMore = mAutoLoadMore;
            return this;
        }

        public RcvHelper create() {
            if (mViewGroup == null) {
                mViewGroup = new ImplNetViewGroup(mRecyclerView.getContext());
            }
            return new RcvHelper(mRecyclerView, mViewGroup, mLoadMore, mAutoLoadMore, mSmartLoadMoreView);
        }
    }

    public RcvHelper register(Class aClass, ItemViewBinder binder) {
        mAdapter.register(aClass, binder);
        return this;
    }

    public RcvHelper setLayoutManager(RecyclerView.LayoutManager LayoutManager) {
        mRecyclerView.setLayoutManager(LayoutManager);
        return this;
    }

    private void resetItemView() {
        if (mItems.size() == 0) {
            mItems.add(new FDLoadMore(FDLoadMore.Status.EMPTY));
            mAdapter.notifyItemInserted(0);

        } else if (mItems.size() == 1 && mItems.get(0) instanceof FDLoadMore) {
            ((FDLoadMore) mItems.get(0)).setStatus(FDLoadMore.Status.EMPTY);
            mAdapter.notifyDataSetChanged();

        } else {
            initLoadMoreView(hasLoadMore ? FDLoadMore.Status.NORMAL : FDLoadMore.Status.MOREOVER);
        }
    }

    private void showLayout(FDLoadMore.Status status) {
        Object item = mItems.get(mItems.size() - 1);
        if (item instanceof FDLoadMore) {
            ((FDLoadMore) item).setStatus(status);
            set(mItems.size() - 1, item);
        } else {
            add(new FDLoadMore(status));
        }
    }

    public void initLoadMoreView(final FDLoadMore.Status status) {
        if (!mLoadMore) return;
        if (mSmartLoadMoreView) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    int position;
                    RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                    if (layoutManager instanceof StaggeredGridLayoutManager) {
                        position = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null)[0];
                    } else {
                        position = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }
                    if (!(position >= mItems.size() - 1)) {
                        showLayout(status);

                    } else if (mItems.get(mItems.size() - 1) instanceof FDLoadMore) {
                        remove(mItems.size() - 1);
                    }
                }
            });
        } else {
            showLayout(status);
        }
    }

    private void checkLoadView() {
        int index = mItems.size() - 1;
        if (index >= 0 && mItems.get(index) instanceof FDLoadMore) {
            mItems.remove(index);
            mAdapter.notifyItemRemoved(index);
        }
    }

    public void add(Object item) {
        checkLoadView();
        mItems.add(item);
        mAdapter.notifyItemInserted(mItems.size() - 1);
        resetItemView();
    }

    public void addAll(List<Object> items) {
        checkLoadView();
        if (items != null && items.size() > 0) {
            int index = mItems.size() - 1;
            if (index > 0 && mItems.get(index) instanceof FDLoadMore) {
                mItems.remove(index);
            }
            mItems.addAll(items);
            mAdapter.notifyDataSetChanged();
        }
        resetItemView();
    }

    public void replaceAll(List<Object> items) {
        mItems.clear();
        addAll(items);
    }

    public void remove(int index) {
        if (mItems.get(index) instanceof FDLoadMore && mItems.size() == 1) {
            mItems.remove(0);
        } else {
            mItems.remove(index);
        }
        mAdapter.notifyItemRemoved(index);
        resetItemView();
    }

    public void set(int position, Object item) {
        mItems.set(position, item);
        mAdapter.notifyItemChanged(position);
    }

    public void clear() {
        mItems.clear();
        mAdapter.notifyDataSetChanged();
        resetItemView();
    }

    public ArrayList<Object> getDatas() {
        return mItems;
    }

    public final int getSize() {
        if (mItems.get(mItems.size() - 1) instanceof FDLoadMore) {
            return mItems.size() - 1;
        }
        return mItems.size();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void hasLoadMore(boolean hasLoadMore) {
        this.hasLoadMore = hasLoadMore;
    }

    public interface OnScrollListener {

        void onScrollUp(boolean isScrollUp);

        void onScrollBottom();
    }

    INetViewGroup.OnNetListener netListener;

    public void setOnNetListener(INetViewGroup.OnNetListener listener) {
        netListener = listener;
        mNetViewGroup.setNetListener(listener);
    }

    private OnScrollListener mScrollListener;

    public void setOnScrollListener(OnScrollListener listener) {
        mScrollListener = listener;
    }

    private void initEvent() {
        mRecyclerView.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                if (mScrollListener != null) mScrollListener.onScrollBottom();
                if (mLoadMore && mAutoLoadMore && hasLoadMore) {
                    netListener.onLoadMore();
                }
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
                int position = (mRecyclerView == null || mRecyclerView.getChildCount() == 0) ? 0 : mRecyclerView.getChildAt(0).getTop();
                if (mScrollListener != null) {
                    mScrollListener.onScrollUp(position >= 0);
                }
            }
        });
    }


}