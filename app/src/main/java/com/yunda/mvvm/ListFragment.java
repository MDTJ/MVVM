package com.yunda.mvvm;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yunda.lib.base_module.db.entity.UserEntity;
import com.yunda.lib.base_module.view.nopage.BaseNoPageListFragment;
import com.yunda.lib.base_module.view.nopage.BaseNoPageRepository;


/**
 * Created by mtt on 2019-12-02
 * Describe
 */
public class ListFragment extends BaseNoPageListFragment<ListViewModuel, UserEntity> {
    @Override
    protected int getDataType() {
        return BaseNoPageRepository.TYPE_DATA_NATIVE;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.adapter_item;
    }

    @Override
    protected void convertItem(BaseViewHolder helper, UserEntity item) {
        helper.setText(R.id.name,item.getName());
    }

    @Override
    public void setListener() {


    }

    @Override
    public void onResume() {
        super.onResume();
        autoRefresh();
    }
}
