package com.yunda.lib.base_module.view.nopage;


import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yunda.lib.base_module.R;
import com.yunda.lib.base_module.databinding.BaseNopageFragmentBinding;
import com.yunda.lib.base_module.mvvm.BaseMVVMFragment;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseNoPageListFragment<VM extends BaseNoPageViewModel,D> extends BaseMVVMFragment<VM, BaseNopageFragmentBinding> implements OnRefreshListener {
    protected List<D> data = new ArrayList<>();
    SmartRefreshLayout swipeRefresh;
    protected NoPageAdapter mAdapter;
    private RecyclerView recycler;


    @Override
    public int getLayoutId() {
        return R.layout.base_nopage_fragment;
    }


    public List<D> getData() {
        return data;
    }

    public void autoRefresh(){
        swipeRefresh.autoRefresh();
        recycler.scrollToPosition(0);
    }

    @Override
    public void initData() {
        recycler = dataBinding.recycler;
        swipeRefresh = dataBinding.swipeRefresh;

        initEmptyViewLayout(getEmptyViewLayout());

        swipeRefresh.setRefreshHeader(new ClassicsHeader(getActivity()));
        swipeRefresh.setEnableLoadMore(false);
        swipeRefresh.setOnRefreshListener(this);
        mAdapter = new NoPageAdapter(getItemLayout());
        recycler.setAdapter(mAdapter);

        observe(((LiveData) viewModel.getData(getDataType())), new OnCallBack<NoPageInterface<D>>() {
            @Override
            public void onSuccess(NoPageInterface<D> body) {
                initAdapter(body.getList());
            }
        });
    }



    /**
     *  {@link BaseNoPageRepository}
     */
    protected abstract int getDataType();

    @Override
    public void showLoading() {
        if (data != null && data.size() > 0) {

        } else {
            super.showLoading();
        }
    }

    @Override
    public void hideOtherLoading() {
        super.hideOtherLoading();
        if (swipeRefresh != null) {
            swipeRefresh.finishRefresh();
        }
    }

    public void initAdapter(List<D> data) {
        this.data.clear();
        this.data.addAll(data);
        mAdapter.setNewData(data);
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
        observe(((LiveData) viewModel.getData(getDataType())), new OnCallBack<NoPageInterface<D>>() {
            @Override
            public void onSuccess(NoPageInterface<D> body) {
                initAdapter(body.getList());
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        observe(((LiveData) viewModel.getData(getDataType())), new OnCallBack<NoPageInterface<D>>() {
            @Override
            public void onSuccess(NoPageInterface<D> body) {
                initAdapter(body.getList());
            }
        });
    }

    protected class NoPageAdapter extends BaseQuickAdapter<D, BaseViewHolder> {

        public NoPageAdapter(@LayoutRes int layoutResId) {
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
