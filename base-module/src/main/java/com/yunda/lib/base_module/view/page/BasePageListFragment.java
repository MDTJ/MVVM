package com.yunda.lib.base_module.view.page;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yunda.lib.base_module.R;
import com.yunda.lib.base_module.databinding.BasePagefragmentBinding;
import com.yunda.lib.base_module.mvvm.BaseMVVMFragment;
import com.yunda.lib.base_module.view.nopage.NoPageInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mtt on 2019-12-03
 * Describe
 */
public abstract class BasePageListFragment<VM extends BasePageViewModel,D> extends BaseMVVMFragment<VM, BasePagefragmentBinding> implements OnRefreshListener , BaseQuickAdapter.RequestLoadMoreListener {
    protected List<D> data = new ArrayList<>();
    SmartRefreshLayout swipeRefresh;
    protected PageAdapter mAdapter;
    private RecyclerView recycler;



    @Override
    public int getLayoutId() {
         return R.layout.base_pagefragment;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        recycler =dataBinding.recycler;
        swipeRefresh = dataBinding.swipeRefresh;

        initEmptyViewLayout(getEmptyViewLayout());
        swipeRefresh.setRefreshHeader(new ClassicsHeader(getActivity()));
        swipeRefresh.setEnableLoadMore(false);
        swipeRefresh.setOnRefreshListener(this);
        mAdapter = new PageAdapter(getItemLayout());
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this, recycler);
        recycler.setAdapter(mAdapter);

    }


    public void autoRefresh(){
        swipeRefresh.autoRefresh();
        recycler.scrollToPosition(0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        viewModel.setNextPage(1);
        observe(viewModel.getData(getDataType()), new OnCallBack<NoPageInterface<D>>() {
            @Override
            public void onSuccess(NoPageInterface<D> body) {
                initAdapter(body.getList(),1);
            }
        });

    }

    @Override
    public void onLoadMoreRequested() {
        observe(viewModel.getData(getDataType()), new OnCallBack<NoPageInterface<D>>() {
            @Override
            public void onSuccess(NoPageInterface<D> body) {
                initAdapter(body.getList(),viewModel.getNextPage()-1);
            }
        });
    }

    @Override
    public void showLoading() {
//        showContent();
    }

    @Override
    public void hideOtherLoading() {
        super.hideOtherLoading();
        if (swipeRefresh != null) {
            swipeRefresh.finishRefresh();
        }
    }


    public void initAdapter(List<D> data,int requestPage) {
        if(requestPage==1){
            this.data.clear();
            this.data.addAll(data);
            mAdapter.setNewData(data);
        }else {
            this.data.addAll(data);
            mAdapter.setNewData(this.data);
            if(this.data.size()==viewModel.getTotalNum()){
                loadMoreOver();
            }else {
                loadMoreComplete();
            }
        }

//        if (mAdapter.getItemCount() > 0 && mPresenter.getCurrentPage() == 1) {
//            recycler.scrollToPosition(0);
//        }
    }

    public void loadMoreOver() {
        swipeRefresh.finishRefresh();
        mAdapter.loadMoreEnd();
    }

    public void loadMoreComplete() {
        swipeRefresh.finishRefresh();
        mAdapter.loadMoreComplete();
    }

    public void loadMoreError() {
        swipeRefresh.finishRefresh();
        mAdapter.loadMoreFail();
    }


    //空页面
    @NonNull
    @IdRes
    private int getEmptyViewLayout() {
        return R.id.swipe_refresh;
    }

    @Override
    public void errorButtonListener() {
        super.errorButtonListener();
        autoRefresh();
    }

    /**
     *  {@link BasePageRepository}
     */
    protected abstract int getDataType();

    protected class PageAdapter extends BaseQuickAdapter<D, BaseViewHolder> {

        public PageAdapter(@LayoutRes int layoutResId) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, D item) {
            convertItem(helper, item);
        }
    }
    protected abstract int getItemLayout();

    protected abstract void convertItem(BaseViewHolder helper, D item);
}
