package com.yunda.lib.base_module.core;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

/**
 * Created by mtt on 2019-11-28
 * Describe
 */
public abstract class BaseFragment<VB extends ViewDataBinding> extends Fragment implements IBaseView{
    protected VB dataBinding;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding=DataBindingUtil.inflate(inflater,getLayoutId(),container,false);
        dataBinding.setLifecycleOwner(this);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListener();
        initData();
    }

}
