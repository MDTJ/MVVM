package com.mtt.lib.base_module.core;

import androidx.annotation.LayoutRes;

/**
 * Created by mtt on 2019-11-22
 * Describe
 */
public interface IBaseView {
    @LayoutRes
    int getLayoutId();

    void setListener();

    void initData();
}
