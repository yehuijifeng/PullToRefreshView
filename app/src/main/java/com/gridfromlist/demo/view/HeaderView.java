package com.gridfromlist.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gridfromlist.demo.R;
import com.gridfromlist.demo.utils.DateUtil;

import java.text.ParseException;


/**
 * Created by yehuijifeng
 * on 2015/12/26.
 * 下拉刷新自定义的头
 */
public class HeaderView extends LinearLayout {

    private LayoutInflater inflater;

    // 下拉刷新视图（头部视图）
    private View headView;
    // 下拉刷新文字和时间
    private TextView custom_header_hint_text, custom_header_time;
    // 下拉图标
    private ImageView custom_header_image;
    //正在加载的状态
    //private ProgressBar custom_header_bar;
    private LinearInterpolator linearInterpolator;//线性插值器，根据时间百分比设置属性百分比
    private Animation animation;//旋转动画
    private String past_time, current_time;
    private RefreshListener refreshListener;

    public RefreshListener getRefreshListener() {
        return refreshListener;
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    /**
     * 下拉刷新监听接口
     */
    public interface RefreshListener {
        void onRefreshPrepare(boolean bl, PtrFrameLayout frame);

        void onRefreshBegin(boolean bl, PtrFrameLayout frame);

        void onRefreshComplete(boolean bl, PtrFrameLayout frame);
    }

    public HeaderView(Context context) {
        super(context);
        initView();
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        inflater = LayoutInflater.from(getContext());//父容器
        past_time = getContext().getResources().getString(R.string.past_time);
        /**
         * 头部
         */
        headView = inflater.inflate(R.layout.base_headerview, this);
        custom_header_hint_text = (TextView) headView.findViewById(R.id.custom_header_hint_text);
        custom_header_time = (TextView) headView.findViewById(R.id.custom_header_time);
        custom_header_image = (ImageView) headView.findViewById(R.id.custom_header_image);
        //custom_header_bar = (ProgressBar) headView.findViewById(R.id.custom_header_bar);
        custom_header_time.setText(DateUtil.getNow(DateUtil.getDatePattern()));
        //设置旋转动画
        animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);//子心旋转
        linearInterpolator = new LinearInterpolator();
        //setInterpolator表示设置旋转速率。
        animation.setInterpolator(linearInterpolator);
        animation.setRepeatCount(-1);//-1表示循环运行
        animation.setDuration(1000);
    }


    /**
     * 重置ui
     */
    public void onUIReset() {
        custom_header_hint_text.setText(getContext().getResources().getString(R.string.header_hint_normal));
        custom_header_image.setImageResource(R.drawable.ic_refresh_arrow);
        custom_header_image.setRotation(0);//图片旋转
    }

    /**
     * 准备刷新
     */
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        custom_header_hint_text.setText(getContext().getResources().getString(R.string.header_hint_normal));
        custom_header_image.setRotation(0);//图片旋转
        getRefreshListener().onRefreshPrepare(true, frame);
        try {
            if (current_time != null)
                custom_header_time.setText(DateUtil.getTimeReduction(current_time));
            else
                custom_header_time.setText(DateUtil.getNow(DateUtil.getDatePattern()));
        } catch (ParseException e) {
            e.printStackTrace();
            custom_header_time.setText(DateUtil.getNow(DateUtil.getDatePattern()));
        }
    }

    /**
     * 开始刷新
     */
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        custom_header_hint_text.setText(getContext().getResources().getString(R.string.header_hint_normal_ing));
        custom_header_image.setImageResource(R.drawable.ic_default_loading);
        custom_header_image.startAnimation(animation);
        getRefreshListener().onRefreshBegin(true, frame);
    }

    /**
     * 松开刷新
     */
    public void onUIRefreshLet() {
        custom_header_image.setImageResource(R.drawable.ic_refresh_arrow);
        custom_header_hint_text.setText(getContext().getResources().getString(R.string.header_hint_ready));
        custom_header_image.setRotation(180);//图片旋转
    }

    /**
     * 完成刷新
     */
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        custom_header_hint_text.setText(getContext().getResources().getString(R.string.header_hint_normal_over));
        custom_header_image.clearAnimation();
        custom_header_image.setImageResource(R.drawable.ic_refresh_arrow);
        getRefreshListener().onRefreshComplete(true, frame);
        current_time = DateUtil.getNow(DateUtil.getDatePattern());
    }

    /**
     * 位置改变
     *
     * @param frame
     * @param isUnderTouch 是否在触摸
     * @param status       状态码：
     *                     PTR_STATUS_INIT = 1; 初始化
     *                     PTR_STATUS_PREPARE = 2; 准备
     *                     PTR_STATUS_LOADING = 3; 加载中
     *                     PTR_STATUS_COMPLETE = 4; 加载完成
     * @param ptrIndicator 指示器
     */
//    @Override
//    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
//        int mOffsetToRefresh = frame.getOffsetToRefresh();
//        /**如果视图的达到下拉刷新高度大于当前位置，并且小于或等于原来的视图的高度则为下拉刷新未达到状态*/
//
//        if (ptrIndicator.getCurrentPosY() > mOffsetToRefresh && ptrIndicator.getLastPosY() <= mOffsetToRefresh) {
//            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
//                custom_header_hint_text.setText(getContext().getResources().getString(R.string.header_hint_ready));
//                custom_header_image.setRotation(180);//图片旋转
//            }
//        } else if (ptrIndicator.getCurrentPosY() < mOffsetToRefresh && ptrIndicator.getLastPosY() > mOffsetToRefresh) {
//            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
//                custom_header_hint_text.setText(getContext().getResources().getString(R.string.header_hint_normal));
//                custom_header_image.setRotation(0);//图片旋转
//            }
//        }
//    }
}
