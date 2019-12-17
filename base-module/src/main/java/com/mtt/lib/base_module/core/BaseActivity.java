package com.mtt.lib.base_module.core;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.gyf.barlibrary.ImmersionBar;
import com.mtt.lib.base_module.R;
import com.mtt.lib.base_module.databinding.BaseModuleToolbarBinding;
import com.mtt.lib.base_module.widget.BaseTextView;

/**
 * Created by mtt on 2019-11-22
 * Describe
 */
public abstract class BaseActivity<VB extends ViewDataBinding> extends AppCompatActivity implements IBaseView {
    private ImmersionBar mImmersionBar;
    private LinearLayout parentLinearLayout;

    protected VB viewDataBinding;
    private BaseModuleToolbarBinding barBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initContentView(getBaseToolBar());
        setContentView(getLayoutId());
        setListener();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isNeedSetStatusBar()){
            mImmersionBar = ImmersionBar.with(this);
            setStatusBarColor(mImmersionBar);
        }
    }

    private void setBackIcon() {
        setSupportActionBar(barBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (null != getToolbar() && isShowBacking()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (isShowRightView()) {
            barBinding.tvRight.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (!isFinishing()) {
            onBackPressed();
        }
        return true;
    }

    private void initContentView(@LayoutRes int layoutResID) {
        if (isShowToolBar()) {
            ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
            viewGroup.removeAllViews();
            parentLinearLayout = new LinearLayout(this);
            parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
            //  add parentLinearLayout in viewGroup
            viewGroup.addView(parentLinearLayout);
            //  add the layout of BaseActivity in parentLinearLayout
            barBinding=DataBindingUtil.inflate(LayoutInflater.from(this),layoutResID,parentLinearLayout,true);
            barBinding.setLifecycleOwner(this);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        //  added the sub-activity layout id in parentLinearLayout
        if (isShowToolBar()) {
            viewDataBinding=DataBindingUtil.inflate(LayoutInflater.from(this),layoutResID,parentLinearLayout,true);
            setBackIcon();
        } else {
//            super.setContentView(layoutResID);
            viewDataBinding=DataBindingUtil.setContentView(this,layoutResID);

        }
        viewDataBinding.setLifecycleOwner(this);


    }


    public BaseTextView getToolbarTitleView() {
        return barBinding.tvTitle;
    }


    public BaseTextView getRightView() {
        return barBinding.tvRight;
    }


    public void setToolBarTitle(CharSequence title) {
        barBinding.setTitle(title+"");
    }

    public void setToolBarTitleColor(int color) {
        barBinding.tvTitle.setTextColor(color);
    }


    public Toolbar getToolbar() {
        return barBinding.toolbar;
    }

    protected boolean isShowBacking() {
        return true;
    }

    protected boolean isShowRightView() {
        return false;
    }


    protected void setStatusBarColor(ImmersionBar immersionBar) {
        immersionBar.statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimary)
                .init();

    }


    protected boolean isShowToolBar() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }
    protected int getBaseToolBar(){
        return R.layout.base_module_toolbar;
    }
    protected boolean isNeedSetStatusBar(){
        return true;
    }



}
