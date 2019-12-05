package com.yunda.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.yunda.lib.base_module.view.page.BasePageViewModel;

import io.reactivex.Observable;

/**
 * Created by mtt on 2019-12-03
 * Describe
 */
public class ListPageModule extends BasePageViewModel<ListPageApi> {
    public ListPageModule(@NonNull Application application) {
        super(application);
    }

    @Override
    protected Observable getObservable() {
        return apiService.getList(getNextPage()+"");
    }

}
