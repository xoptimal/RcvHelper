package com.xoptimal.revhelper.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;


public abstract class OnRcvScrollListener extends RecyclerView.OnScrollListener {

    private static final int TYPE_LINEAR = 0;

    private static final int TYPE_GRID = 1;

    private static final int TYPE_STAGGERED_GRID = 2;

    private int[] mLastPositions;

    private int mLastVisibleItemPosition;

    private static final int HIDE_THRESHOLD = 20;

    private int mDistance = 0;

    private boolean mIsScrollDown = true;

    private int mScrolledYDistance = 0;

    private int mScrolledXDistance = 0;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int                        firstVisibleItemPosition = 0;
        RecyclerView.LayoutManager layoutManager            = recyclerView.getLayoutManager();
        int                        type                     = judgeLayoutManager(layoutManager);
        firstVisibleItemPosition = calculateFirstVisibleItemPos(type, layoutManager, firstVisibleItemPosition);
        calculateScrollUpOrDown(firstVisibleItemPosition, dy);
        mScrolledXDistance += dx;
        mScrolledYDistance += dy;
        mScrolledXDistance = (mScrolledXDistance < 0) ? 0 : mScrolledXDistance;
        mScrolledYDistance = (mScrolledYDistance < 0) ? 0 : mScrolledYDistance;
        onScrolled(mScrolledXDistance, mScrolledYDistance);
    }


    private int judgeLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            return TYPE_GRID;
        } else if (layoutManager instanceof LinearLayoutManager) {
            return TYPE_LINEAR;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return TYPE_STAGGERED_GRID;
        } else {
            throw new RuntimeException("Unsupported LayoutManager used. Valid ones are " + "LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
        }
    }

    private int calculateFirstVisibleItemPos(int type, RecyclerView.LayoutManager layoutManager, int firstVisibleItemPosition) {
        switch (type) {
        case TYPE_LINEAR:
            mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            break;
        case TYPE_GRID:
            mLastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            break;
        case TYPE_STAGGERED_GRID:
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            if (mLastPositions == null) {
                mLastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            }
            mLastPositions = staggeredGridLayoutManager.findLastVisibleItemPositions(mLastPositions);
            mLastVisibleItemPosition = findMax(mLastPositions);
            staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(mLastPositions);
            firstVisibleItemPosition = findMax(mLastPositions);
            break;
        }
        return firstVisibleItemPosition;
    }

    private void calculateScrollUpOrDown(int firstVisibleItemPosition, int dy) {
        if (firstVisibleItemPosition == 0) {
            if (!mIsScrollDown) {
                onScrollDown();
                mIsScrollDown = true;
            }
        } else {
            if (mDistance > HIDE_THRESHOLD && mIsScrollDown) {
                onScrollUp();
                mIsScrollDown = false;
                mDistance = 0;
            } else if (mDistance < -HIDE_THRESHOLD && !mIsScrollDown) {
                onScrollDown();
                mIsScrollDown = true;
                mDistance = 0;
            }
        }
        if ((mIsScrollDown && dy > 0) || (!mIsScrollDown && dy < 0)) {
            mDistance += dy;
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager layoutManager    = recyclerView.getLayoutManager();
        int                        visibleItemCount = layoutManager.getChildCount();
        int                        totalItemCount   = layoutManager.getItemCount();
        if (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItemPosition >= totalItemCount - 1) {
            onBottom();
        }
    }

    public void onScrollUp() {
    }

    public void onScrollDown() {
    }

    public abstract void onBottom();

    public void onScrolled(int distanceX, int distanceY) {

    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            max = Math.max(max, value);
        }
        return max;
    }
}
