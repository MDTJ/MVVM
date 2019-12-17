package com.mtt.mvvm;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.mtt.lib.base_module.view.nopage.BaseNoPageListFragment;
import com.mtt.lib.base_module.view.nopage.BaseNoPageRepository;


/**
 * Created by mtt on 2019-12-02
 * Describe
 */
public class ListFragment extends BaseNoPageListFragment<ListViewModuel, TestListBean.Bean> {
    @Override
    protected int getDataType() {
        return BaseNoPageRepository.TYPE_DATA_HTTP;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.adapter_item;
    }

    @Override
    protected void convertItem(BaseViewHolder helper, TestListBean.Bean item) {
        helper.setText(R.id.name,item.getTitle());
        helper.getView(R.id.shanchu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Asdadad",item.getTitle()+"11");
            }
        });

    }



    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected int isNeedRightMenu() {
        return R.layout.item_right;
    }

    @Override
    protected void setOnItemClickListener(TestListBean.Bean item) {
        Log.e("sadada",item.getTitle());
    }
}
