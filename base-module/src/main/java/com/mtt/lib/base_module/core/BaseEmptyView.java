package com.mtt.lib.base_module.core;

/**
 * Created by mtt on 2019-11-22
 * Describe
 */
public interface BaseEmptyView<M> {
    void showEmpty();

    void showDataError(String msg);

    void showNetError(String msg);

    void showContent();

    void showLoading();

    void showToast(String msg);

    void showWaiting();

    void onSuccess(M body);
}
