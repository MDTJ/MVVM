package com.mtt.mvvm;

import android.view.View;

import com.mtt.lib.base_module.mvvm.BaseMVVMFragment;
import com.mtt.mvvm.databinding.FragmentBinding;

/**
 * Created by mtt on 2019-11-28
 * Describe
 */
public class MainFragment extends BaseMVVMFragment<MainViewModel, FragmentBinding> {
    @Override
    public int getLayoutId() {
        return R.layout.fragment;
    }

    @Override
    public void setListener() {
        dataBinding.setOnclick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observe(viewModel.getData(), new OnCallBack<UserBean>() {
                    @Override
                    public void onSuccess(UserBean body) {
                    }
                });
            }
        });
    }

    @Override
    public void initData() {
        initEmptyViewLayout(R.id.empty);

    }

}
