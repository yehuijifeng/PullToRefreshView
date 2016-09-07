package com.gridfromlist.demo.view;

import android.widget.AbsListView;


/**
 * Created by Luhao on 2016/6/28.
 * listview和gridview的监听
 */
public class OnScollListener implements AbsListView.OnScrollListener {

    protected SidingStatusListener sidingStatusListener;
    protected boolean isLoadMore = true, isLoadStatus, isLoadComplete = true;
    private boolean isLoadSuccess = true;//该状态用于控制重复的加载，当第一次加载更多没有完成的时候，该状态一直是false
    private int isLoadCompletes = 1;//2:屏幕没有显示完全；1：屏幕显示完全了，说明数据不够一屏幕的显示，默认值
    protected OnScollListenerStatus onScollListenerStatus;//是否显示完全，和是否可以加载更多的接口

    public OnScollListenerStatus getOnScollListenerStatus() {
        return onScollListenerStatus;
    }

    public void setOnScollListenerStatus(OnScollListenerStatus onScollListenerStatus) {
        this.onScollListenerStatus = onScollListenerStatus;
    }

    public boolean isLoadComplete() {
        //LogUtils.i("检索是否显示完全：" + isLoadComplete);
        return isLoadCompletes == 1;
    }

    public boolean isLoadSuccess() {
        return isLoadSuccess;
    }

    public void setLoadSuccess(boolean loadSuccess) {
        isLoadSuccess = loadSuccess;
    }

    public boolean isLoadMore() {
        return isLoadMore;
    }

    public void setLoadMore(boolean loadMore) {
        isLoadMore = loadMore;
    }

    public void setSidingStatusListener(SidingStatusListener sidingStatusListener) {
        this.sidingStatusListener = sidingStatusListener;
    }

    public interface SidingStatusListener {
        void onScrollStateChanged(AbsListView view, int scrollState);
    }

    public interface OnScollListenerStatus {
        void onStartLoading();

        /**
         * 2:屏幕没有显示完全；1：屏幕显示完全了，说明数据不够一屏幕的显示，默认值
         *
         * @param isLoadCompleteStatus
         */
        void isLoadComplete(int isLoadCompleteStatus);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (sidingStatusListener != null)
            sidingStatusListener.onScrollStateChanged(view, scrollState);
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {//滚动停止的时候
            //LogUtils.i("滑动后手指抬起");
            if (isLoadStatus) {
                //LogUtils.i("滑到最后了");
                if (isLoadSuccess()) {
                    // LogUtils.i("有效加载，没有重复");
                    getOnScollListenerStatus().onStartLoading();//可以开始加载了
                    isLoadSuccess = false;
                }
            }
        } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            //LogUtils.i("滑动中");
        } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
            //LogUtils.i("到底部或者头部了");
        }
    }

    /**
     * @param view
     * @param firstVisibleItem 第一个
     * @param visibleItemCount 可见的
     * @param totalItemCount   总数
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!isLoadMore()) return;
        isLoadComplete = visibleItemCount >= totalItemCount;
        isLoadCompletes = isLoadComplete ? 1 : 2;
        if (getOnScollListenerStatus() != null)
            getOnScollListenerStatus().isLoadComplete(isLoadCompletes);
        if (!isLoadComplete) {//如果当前数据一屏幕内就已经现实完，则表示没有更多数据
            int lastItemIndex = firstVisibleItem + visibleItemCount;
//            LogUtils.i("当前行：" + lastItemIndex);
//            LogUtils.i("第一个：" + firstVisibleItem);
//            LogUtils.i("可见的：" + visibleItemCount);
//            LogUtils.i("总数：" + totalItemCount);
            isLoadStatus = lastItemIndex == totalItemCount;
        }
        //LogUtils.i("是否显示完全：" + isLoadCompletes);
    }
}
