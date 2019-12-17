package com.mtt.lib.view.emptyview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.mtt.lib.view.R;

public class EmptyViewLayout {
	private final static int TYPE_EMPTY = 1;
	private final static int TYPE_LOADING = 2;
	private final static int TYPE_DATA_ERROR = 3;
	private final static int TYPE_SUCCESS = 4;
	private final static int TYPE_NET_ERROR = 5;
	private final static int TYPE_NOBUY = 6;


    private final View contentView;
    private final LayoutInflater mInflate;
    private final int mAnimationDuration;
    private ViewGroup commenViewStub;
    private ViewStub mEmptyView, mDataErrorView, mNetErrorView, mLoadingView,mNoBuyView;
    private ImageView mEmptyViewIv, mDataErrorViewIv, mNetErrorViewIv, mLoadingViewIv,mNoBuyViewIv;
    private TextView mEmptyViewTv, mDataErrorViewTv, mNetErrorViewTv, mLoadingViewTv, mDataErrorViewBt,mEmptyViewBt;
    private Button  mNetErrorViewBt;//, mLoadingViewBt;
    private String mLoadingTv, mDataErrorTv, mNetErrorTv, mEmptyTv;
    private AnimationDrawable animationDrawable;
    private Drawable loaingDrable;
    private int mEmptyIv = -1;
    private int mDataErrorIv = -1;
    private int mNetErrorIv = -1;
    private int currentType = -1;
    private ErrorViewListener mErrorViewListener;

    public EmptyViewLayout(Context context, View contentView) {
        this.contentView = contentView;
        mInflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAnimationDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
        initAddView();
    }

    public void setErrorListener(ErrorViewListener mErrorViewListener) {
        this.mErrorViewListener = mErrorViewListener;
    }

    private void initAddView() {
		initNormalViewStub();
		//替换原来的位置
        ViewGroup parent = (ViewGroup) contentView.getParent();
        int pos = parent.indexOfChild(contentView);
        if (pos == -1) {
            ((ViewGroup) contentView.getParent()).addView(commenViewStub);
        } else {
            parent.addView(commenViewStub, parent.indexOfChild(contentView));
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private void crossfadeView() {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        contentView.setAlpha(0f);
        contentView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        contentView.animate().alpha(1f).setDuration(mAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        commenViewStub.animate().alpha(0f)
                .setDuration(mAnimationDuration);
        mLoadingView.animate().alpha(0f).setDuration(mAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoadingView.setVisibility(View.GONE);
                        mLoadingView.setAlpha(1f);
                        commenViewStub.setVisibility(View.GONE);
                        commenViewStub.setAlpha(1f);
                    }
                });
    }


	private void initNormalViewStub() {
		if (commenViewStub == null) {
            commenViewStub = (ViewGroup) mInflate.inflate(R.layout.empty_view_stub, null);
            ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
            if (layoutParams != null) {
                commenViewStub.setLayoutParams(layoutParams);
            }
        }
    }

    public void showEmpty() {
        currentType = TYPE_EMPTY;
        showViewStatus(currentType);
    }
    public void showNoBuy(){
        currentType = TYPE_NOBUY;
        showViewStatus(currentType);
    }

    public void showDataError() {
        currentType = TYPE_DATA_ERROR;
        showViewStatus(currentType);
    }

    public void showNetError() {
        currentType = TYPE_NET_ERROR;
        showViewStatus(currentType);
    }

    public void showContent() {
        currentType = TYPE_SUCCESS;
        showViewStatus(currentType);
    }

    public void showLoading() {
        currentType = TYPE_LOADING;
        showViewStatus(currentType);
    }

    public void showViewStatus(int status) {
        switch (status) {
            case TYPE_NOBUY:
                commenViewStub.setVisibility(View.VISIBLE);
                initNoBuyView();
                closeAllViewExcept(mNoBuyView);
                stopLoadingAnim();
                break;
            case TYPE_EMPTY:
                commenViewStub.setVisibility(View.VISIBLE);
                initEmptyView();
                setTextviewString(mEmptyViewTv, mEmptyTv);
                setImageView(mEmptyViewIv, mEmptyIv);
                closeAllViewExcept(mEmptyView);
                stopLoadingAnim();
                break;
            case TYPE_LOADING:
                commenViewStub.setVisibility(View.VISIBLE);
                initLoadingView();
                setTextviewString(mLoadingViewTv, mLoadingTv);
                if (loaingDrable != null) {
                    mLoadingViewIv.setImageDrawable(loaingDrable);
                    animationDrawable = null;
                }
                closeAllViewExcept(mLoadingView);
                startLoadingAnim();
                break;
            case TYPE_DATA_ERROR:
                stopLoadingAnim();
                commenViewStub.setVisibility(View.VISIBLE);
                initDataErrorView();
                setTextviewString(mDataErrorViewTv, mDataErrorTv);
                setImageView(mDataErrorViewIv, mDataErrorIv);
                closeAllViewExcept(mDataErrorView);
                break;
            case TYPE_SUCCESS:
                commenViewStub.setVisibility(View.GONE);
                stopLoadingAnim();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                    if (contentView != null) {
                        if (mLoadingView != null
                                && mLoadingView.getVisibility() == View.VISIBLE) {
                            crossfadeView();
                        } else {
                            commenViewStub.setVisibility(View.GONE);
                            closeAllViewExcept(contentView);
                        }
                        contentView.setEnabled(true);
                    }
                } else {
                    closeAllViewExcept(contentView);
                    contentView.setEnabled(true);
                }
                break;
            case TYPE_NET_ERROR:
                stopLoadingAnim();
                commenViewStub.setVisibility(View.VISIBLE);
                initNetErrorView();
                setTextviewString(mNetErrorViewTv, mNetErrorTv);
                setImageView(mNetErrorViewIv, mNetErrorIv);
                closeAllViewExcept(mNetErrorView);
                break;
            default:
                break;
        }
    }

    private void closeAllViewExcept(View view) {
        setViewGone(mLoadingView);
        setViewGone(mDataErrorView);
        setViewGone(mNetErrorView);
        setViewGone(mEmptyView);
        setViewGone(contentView);
        setViewGone(mNoBuyView);
        setViewVisible(view);
//		if (mLoadingView!=null&&st == View.VISIBLE) {
//			startLoadingAnim();
//		} else {
//			stopLoadingAnim();
//		}
    }

    private void setViewGone(View viewVisible) {
        if (viewVisible != null) viewVisible.setVisibility(View.GONE);
    }

    private void setViewVisible(View viewVisible) {
        if (viewVisible != null) viewVisible.setVisibility(View.VISIBLE);
    }
    private void initNoBuyView(){
        if (mNoBuyView == null) {
            mNoBuyView = (ViewStub) commenViewStub.findViewById(R.id.vs_noBuy);
        }
    }

    private void initEmptyView() {
        if (mEmptyView == null) {
            mEmptyView = (ViewStub) commenViewStub.findViewById(R.id.vs_empty);
            View view = mEmptyView.inflate();
            mEmptyViewIv = (ImageView) view.findViewById(R.id.app_view_iv);
            mEmptyViewTv = (TextView) view.findViewById(R.id.app_message_tv);
            mEmptyViewBt = (TextView) view.findViewById(R.id.app_action_empty_btn);
            mEmptyViewBt.setOnClickListener(mButtonClickListener);
        }
    }

    private void initDataErrorView() {
        if (mDataErrorView == null) {
            mDataErrorView = (ViewStub) commenViewStub.findViewById(R.id.vs_data_error);
            View view = mDataErrorView.inflate();
            mDataErrorViewIv = (ImageView) view.findViewById(R.id.app_view_iv);
            mDataErrorViewTv = (TextView) view.findViewById(R.id.app_message_tv);
            mDataErrorViewBt = (TextView) view.findViewById(R.id.app_action_empty_btn);
            mDataErrorViewBt.setOnClickListener(mButtonClickListener);
        }
    }

    private void initNetErrorView() {
        if (mNetErrorView == null) {
            mNetErrorView = (ViewStub) commenViewStub.findViewById(R.id.vs_net_error);
            View view = mNetErrorView.inflate();
            mNetErrorViewIv = (ImageView) view.findViewById(R.id.app_view_iv);
            mNetErrorViewTv = (TextView) view.findViewById(R.id.app_message_tv);
            mNetErrorViewBt = (Button) view.findViewById(R.id.app_action_empty_btn);
            mNetErrorViewBt.setOnClickListener(mButtonClickListener);
        }
    }

    private void initLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = (ViewStub) commenViewStub.findViewById(R.id.vs_loading);
            View view = mLoadingView.inflate();
            mLoadingViewIv = (ImageView) view.findViewById(R.id.app_view_iv);
            mLoadingViewTv = (TextView) view.findViewById(R.id.app_message_tv);
        }
    }

    public void setLoadingMessage(String str) {
        this.mLoadingTv = str;
    }

    public void setEmptyMessage(String str) {
        this.mEmptyTv = str;
    }

    public void setDataErrorMessage(String str) {
        if(mDataErrorViewTv==null){
            this.mDataErrorTv = str;
        }else {
            mDataErrorViewTv.setText(str);
            this.mDataErrorTv = str;
        }


    }

    public void setNetErrorMessage(String str) {
        this.mNetErrorTv = str;
    }

    public void setLoadingImage(Drawable drawable) {
        this.loaingDrable = drawable;
    }

    public void setEmptyImage(int imageResource) {
        this.mEmptyIv = imageResource;
    }

    public void setDataErrorImage(int imageResource) {
        this.mDataErrorIv = imageResource;
    }


    public void setEmptyGoneOrDisplay(int msgDisplay,int btnDisplay){
        mEmptyViewBt.setVisibility(btnDisplay);
        mEmptyViewTv.setVisibility(msgDisplay);

    }
    public void setDataErrorGoneOrDisplay(int msgDisplay,int btnDisplay){
        mDataErrorViewBt.setVisibility(btnDisplay);
        mDataErrorViewTv.setVisibility(msgDisplay);
    }

    public void setNetErrorImage(int imageResource) {
        this.mNetErrorIv = imageResource;
    }

    private void setImageView(ImageView imageView, int imageResource) {
        if (imageResource != -1) imageView.setImageResource(imageResource);
    }

    private void setTextviewString(TextView tv, String str) {
        if (str != null) tv.setText(str);
    }

    private void startLoadingAnim() {
        if (animationDrawable == null) {
            animationDrawable = (AnimationDrawable) mLoadingViewIv.getDrawable();
            animationDrawable.start();
        } else {
            animationDrawable.start();
        }
    }

    private void stopLoadingAnim() {
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mErrorViewListener != null) {
                mErrorViewListener.errorButtonListener();
            }
        }
    };

    public interface ErrorViewListener {
        void errorButtonListener();
    }
}
