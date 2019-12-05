package com.yunda.mvvm;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yunda.lib.base_module.view.page.BasePageListFragment;
import com.yunda.lib.base_module.view.page.BasePageRepository;

/**
 * Created by mtt on 2019-12-03
 * Describe
 */
public class ListPageFragment extends BasePageListFragment<ListPageModule, TestListBean.Bean> {


    @Override
    protected int getDataType() {
        return BasePageRepository.TYPE_DATA_HTTP;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.adapter_item;
    }

    @Override
    protected void convertItem(BaseViewHolder helper, TestListBean.Bean item) {
        helper.setText(R.id.name,item.getTitle());
    }

    @Override
    public void onResume() {
        super.onResume();
        autoRefresh();
    }
}