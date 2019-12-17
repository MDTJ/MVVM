package com.mtt.lib.base_module.view.nopage;


import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
import com.mtt.lib.base_module.R;
import com.mtt.lib.base_module.databinding.BaseNopageFragmentBinding;
import com.mtt.lib.base_module.mvvm.BaseMVVMFragment;
import com.mtt.lib.base_module.widget.SideslipRecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseNoPageListFragment<VM extends BaseNoPageViewModel,D> extends BaseMVVMFragment<VM, BaseNopageFragmentBinding> implements OnRefreshListener {
    protected List<D> data = new ArrayList<>();
    SmartRefreshLayout swipeRefresh;
    protected BaseQuickAdapter mAdapter;
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
    public void setListener() {

    }

    @Override
    public void initData() {
        recycler = dataBinding.recycler;
        swipeRefresh = dataBinding.swipeRefresh;

        initEmptyViewLayout(getEmptyViewLayout());

        swipeRefresh.setRefreshHeader(new ClassicsHeader(getActivity()));
        swipeRefresh.setEnableLoadMore(false);
        swipeRefresh.setOnRefreshListener(this);
        if (isNeedRightMenu() != 0) {
            mAdapter = new HaveMenuPageAdapter();
        } else {
            mAdapter = new NoPageAdapter(getItemLayout());
        }
        recycler.setAdapter(mAdapter);

        observe(((LiveData) viewModel.getData(getDataType())), new OnCallBack<NoPageInterface<D>>() {
            @Override
            public void onSuccess(NoPageInterface<D> body) {
                initAdapter(body.getList());
            }
        });
        autoRefresh();
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



    protected class HaveMenuPageAdapter extends BaseQuickAdapter<D, BaseViewHolder> {

        private SideslipRecyclerViewItem cruView;//当前滑出的view
        private int width;
        private RelativeLayout.LayoutParams params;
        private RelativeLayout.LayoutParams params2;

        public HaveMenuPageAdapter() {
            super(R.layout.base_item_recycle, data);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            width = displaymetrics.widthPixels;
            params = new RelativeLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params2.addRule(RelativeLayout.CENTER_VERTICAL);
            params2.addRule(RelativeLayout.RIGHT_OF, R.id.base_item_content_layout);

        }

        @Override
        protected View getItemView(int layoutResId, ViewGroup parent) {
            View view = mLayoutInflater.inflate(layoutResId, parent, false);
            if (view instanceof SideslipRecyclerViewItem) {
                ((SideslipRecyclerViewItem) view).setOnScrollListener(new SideslipRecyclerViewItem.OnScrollListener() {
                    @Override
                    public void onScrolledViewListener(SideslipRecyclerViewItem view) {
                        cruView = view;
                    }

                    @Override
                    public void onClickDownListener() {
                        if (cruView != null) {
                            cruView.reset();
                            cruView = null;
                        }
                    }

                    @Override
                    public boolean onIntercept() {
                        if (cruView == null) {
                            return false;
                        }
                        return cruView != view;
                    }
                });
            }

            RelativeLayout contentLayout = view.findViewById(R.id.base_item_content_layout);
            if (contentLayout != null) {
                contentLayout.setLayoutParams(params);
                mLayoutInflater.inflate(getItemLayout(), contentLayout, true);
            }
            RelativeLayout rightLayout = view.findViewById(R.id.base_item_right_Layout);
            if (rightLayout != null) {
                rightLayout.setLayoutParams(params2);
                mLayoutInflater.inflate(isNeedRightMenu(), rightLayout, true);

            }


            return view;
        }

        @Override
        protected void convert(BaseViewHolder helper, D item) {
            convertItem(helper, item);
            helper.getView(R.id.base_item_content_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cruView != null) {
                        cruView.reset();
                        cruView=null;
                        return;
                    }
                    BaseNoPageListFragment.this.setOnItemClickListener(item);
                }
            });
        }
    }

    protected abstract int getItemLayout();

    protected abstract void convertItem(BaseViewHolder helper, D item);

    protected void setOnItemClickListener(D item) {

    }

    //设置菜单的布局
    protected int isNeedRightMenu() {
        return 0;
    }
}
