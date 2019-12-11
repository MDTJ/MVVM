package com.yunda.lib.base_module.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by mtt on 2019-12-06
 * Describe
 */
public class SideslipRecyclerViewItem extends HorizontalScrollView {


    private OnScrollListener onScrollListener;

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public SideslipRecyclerViewItem(Context context) {
        super(context);
        init();

    }

    public SideslipRecyclerViewItem(Context context, AttributeSet attrs) {

        super(context, attrs);
        init();
    }

    public SideslipRecyclerViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    private boolean isLeft = true;//默认左边
    private int range=10;

    public void reset(){
        isLeft=true;
        smoothScrollTo(0,0);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN&&onScrollListener.onIntercept()){
            return super.onInterceptTouchEvent(ev)||true;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onScrollListener.onClickDownListener();
            return true;
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            Log.e("adadad","getScrollX"+getScrollX());
            if (isLeft) {
                if (getScrollX() > range) {
                    isLeft = false;
                    onScrollListener.onScrolledViewListener(this);
                    smoothScrollTo(getWidth(), 0);
                    Log.e("adadad","scrollTo(getWidth(), 0)1"+getScrollX());
                } else {
                    smoothScrollTo(0, 0);
                    Log.e("adadad","scrollTo(0, 0)2;"+getScrollX());
                }
            } else {
                if (getScrollX() < 500) {
                    isLeft = true;
                    smoothScrollTo(0, 0);
                    Log.e("adadad","scrollTo(0, 0)3;"+getScrollX());
                } else {
                    smoothScrollTo(getWidth(), 0);
                    Log.e("adadad","scrollTo(getWidth(), 0)4;"+getScrollX());
                }
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    public interface OnScrollListener{
        void onScrolledViewListener(SideslipRecyclerViewItem view);
        void onClickDownListener();
        boolean onIntercept();
    }

}
