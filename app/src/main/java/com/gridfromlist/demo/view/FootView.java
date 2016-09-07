package com.gridfromlist.demo.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gridfromlist.demo.R;

/**
 * Created by yehuijifeng
 * on 2015/12/26.
 * 加载更多的视图
 */
public class FootView extends LinearLayout {

    private TextView custom_footer_hint_text;
    private ProgressBar custom_footer_bar;
    private View root;
    private LoadMoreListener loadMoreListener;

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public FootView(Context context) {
        super(context);
        initView();
    }

    public FootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        root = LayoutInflater.from(getContext()).inflate(R.layout.base_footerview, this);
        custom_footer_hint_text = (TextView) root.findViewById(R.id.custom_footer_hint_text);
        custom_footer_hint_text.setText(getContext().getResources().getString(R.string.foot_hinit_normal));
        custom_footer_bar = (ProgressBar) root.findViewById(R.id.custom_footer_bar);
        custom_footer_bar.setVisibility(View.INVISIBLE);
    }

    /**
     * 上拉加载
     */
    public interface LoadMoreListener {
        void onLoadMorePrepare(boolean bl);

        void onLoadMoreBegin(boolean bl);

        void onLoadMoreComplete(boolean bl);
    }

    /**
     * 准备加载
     */
    public void onFootPrepare(boolean isLoadComplete) {
        custom_footer_hint_text.setText(getContext().getResources().getString(isLoadComplete ? R.string.foot_hinit_normal_no_data : R.string.foot_hinit_normal));
        custom_footer_bar.setVisibility(View.INVISIBLE);
        if (loadMoreListener != null) loadMoreListener.onLoadMorePrepare(true);
    }

    /**
     * 开始加载
     */
    public void onFootViewBegin() {
        custom_footer_hint_text.setText(getContext().getResources().getString(R.string.foot_hinit_normal_ing));
        custom_footer_bar.setVisibility(View.VISIBLE);
        if (loadMoreListener != null) loadMoreListener.onLoadMoreBegin(true);
    }

    /**
     * 松开加载
     */
    public void onFootViewLog() {
        custom_footer_hint_text.setText(getContext().getResources().getString(R.string.foot_hinit_normal_ready));
        custom_footer_bar.setVisibility(View.INVISIBLE);
    }

    /**
     * 完成加载
     */
    public void onFootViewComplete() {
        custom_footer_hint_text.setText(getContext().getResources().getString(R.string.foot_hinit_normal_over));
        custom_footer_bar.setVisibility(View.INVISIBLE);
    }

    /**
     * 加载完全部数据
     */
    public void onFootViewAll() {
        custom_footer_hint_text.setText(getContext().getResources().getString(R.string.foot_hinit_normal_no_data));
        custom_footer_bar.setVisibility(View.INVISIBLE);
        if (loadMoreListener != null) loadMoreListener.onLoadMoreComplete(true);
    }


    /**
     * 位置改变
     *
     * @param isUnderTouch    是否在触摸
     * @param status          状态码：
     *                        PTR_STATUS_INIT = 1; 初始化
     *                        PTR_STATUS_PREPARE = 2; 准备
     *                        PTR_STATUS_LOADING = 3; 加载中
     *                        PTR_STATUS_COMPLETE = 4; 加载完成
     * @param oldPosition     原来的位置
     * @param currentPosition 当前位置
     * @param oldPercent      原来的百分比
     * @param currentPercent  当前的百分比
     */
    public void onFootViewChange(boolean isUnderTouch, byte status, int oldPosition, int currentPosition, float oldPercent, float currentPercent) {

        final int mOffsetToRefresh = root.getHeight();
        /**如果视图的达到下拉刷新高度大于当前位置，并且小于或等于原来的视图的高度则为下拉刷新未达到状态*/
        if (currentPosition > mOffsetToRefresh && oldPosition <= mOffsetToRefresh) {
            if (isUnderTouch) {
                custom_footer_hint_text.setText(getContext().getResources().getString(R.string.foot_hinit_normal_ready));
                custom_footer_bar.setVisibility(View.INVISIBLE);
            }
        }
    }
}
