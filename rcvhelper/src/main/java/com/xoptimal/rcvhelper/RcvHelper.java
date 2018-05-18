package com.xoptimal.rcvhelper;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.xoptimal.rcvhelper.entity.NetStatus;
import com.xoptimal.rcvhelper.listener.OnRcvScrollListener;
import com.xoptimal.rcvhelper.view.INetViewGroup;
import com.xoptimal.rcvhelper.view.ImplNetViewGroup;
import com.xoptimal.rcvhelper.viewholder.NetViewHolder;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.Linker;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.xoptimal.rcvhelper.entity.NetStatus.Status;

/**
 * Created by Freddie on 2016/03/12 .
 * Description:
 */
public class RcvHelper {

    private RecyclerView     mRecyclerView;
    private MultiTypeAdapter mAdapter;
    private boolean          mLoadMore;
    private boolean          hasLoadMore;
    private boolean          mSmartLoadMoreView;
    private boolean          mAutoLoadMore;
    private Items            mItems;



    protected RcvHelper(RecyclerView recyclerView, boolean loadMore, boolean autoLoadMore, boolean smartLoadMoreView) {
        mLoadMore = loadMore;
        mAutoLoadMore = autoLoadMore;
        mSmartLoadMoreView = smartLoadMoreView;
        mRecyclerView = recyclerView;
        mAdapter = new MultiTypeAdapter();
        mAdapter.setItems(mItems = new Items());
        mAdapter.register(NetStatus.class).to(
                new NetViewHolder(R.layout.fd_view_normal),
                new NetViewHolder(R.layout.fd_view_loading),
                new NetViewHolder(R.layout.fd_view_error),
                new NetViewHolder(R.layout.fd_view_moreover),
                new NetViewHolder(R.layout.fd_view_empty)
        ).withLinker(new Linker<NetStatus>() {
            @Override
            public int index(int position, @NonNull NetStatus netStatus) {
                switch (netStatus.getStatus()) {
                    case LOADING:
                        return 1;
                    case ERROR:
                        return 2;
                    case MOREOVER:
                        return 3;
                    case EMPTY:
                        return 4;
                    default:
                        return 0;
                }
            }
        });
        if (mRecyclerView.getLayoutManager() == null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        }
        mRecyclerView.setAdapter(mAdapter);
        resetItemView();
        if (mLoadMore) initEvent();
    }

    public static class Builder {

        private RecyclerView  mRecyclerView;

        private boolean mLoadMore          = true;
        private boolean mSmartLoadMoreView = true;
        private boolean mAutoLoadMore      = true;

        public Builder(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
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
            return new RcvHelper(mRecyclerView, mLoadMore, mAutoLoadMore, mSmartLoadMoreView);
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
            mItems.add(new NetStatus(Status.EMPTY));
            mAdapter.notifyItemInserted(0);

        } else if (mItems.size() == 1 && mItems.get(0) instanceof NetStatus && ((NetStatus) mItems.get(0)).getStatus() != Status.EMPTY) {
            ((NetStatus) mItems.get(0)).setStatus(Status.EMPTY);
            mAdapter.notifyDataSetChanged();

        } else if (mItems.size() > 1) {
            initLoadMoreView(hasLoadMore ? Status.NORMAL : Status.MOREOVER);
        }
    }

    private void showLayout(Status status) {
        Object item = mItems.get(mItems.size() - 1);
        if (item instanceof NetStatus) {
            ((NetStatus) item).setStatus(status);
            set(mItems.size() - 1, item);
        } else {
            add(new NetStatus(status));
        }
    }

    public void initLoadMoreView(final Status status) {
        if (!mLoadMore) return;
        if (mSmartLoadMoreView) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    int                        lastPosition, firstPosition;
                    RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                    if (layoutManager instanceof StaggeredGridLayoutManager) {
                        lastPosition = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null)[0];
                        firstPosition = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null)[0];
                    } else {
                        lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        firstPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    }

                    int count = mItems.size() - 1;

                    if (!(lastPosition >= count && firstPosition == 0)) {
                        showLayout(status);

                    } else if (firstPosition == 0 && mItems.get(count) instanceof NetStatus) {
                        mItems.remove(count);
                        mAdapter.notifyItemRemoved(count);
                    }
                }

            });
        } else {
            showLayout(status);
        }
    }

    private void checkLoadView() {
        int index = mItems.size() - 1;
        if (index >= 0 && mItems.get(index) instanceof NetStatus) {
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
            if (index > 0 && mItems.get(index) instanceof NetStatus) {
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
        if (index >= 0) {
            mItems.remove(index);
            mAdapter.notifyItemRemoved(index);
        }
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
        if (mItems.get(mItems.size() - 1) instanceof NetStatus) {
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