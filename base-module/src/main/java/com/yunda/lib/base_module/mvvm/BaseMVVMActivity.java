package com.yunda.lib.base_module.mvvm;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.yunda.lib.base_module.core.BaseBean;
import com.yunda.lib.base_module.core.BaseEmptyActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by mtt on 2019-11-22
 * Describe
 */
public abstract class BaseMVVMActivity<VM extends BaseViewModel,VB extends ViewDataBinding,M> extends BaseEmptyActivity<VB,M> {
    protected VM viewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createViewModel();
    }

    public void createViewModel() {
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) ViewModelProviders.of(this).get(modelClass);
            viewModel.setAutoDisposeConverter(AutoDispose.<BaseBean<M>>autoDisposable(AndroidLifecycleScopeProvider.from(this)));
        }
    }
}
