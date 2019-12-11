package com.yunda.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.yunda.lib.base_module.view.nopage.BaseNoPageViewModel;

import io.reactivex.Observable;

/**
 * Created by mtt on 2019-12-02
 * Describe
 */
public class ListViewModuel extends BaseNoPageViewModel<ListPageApi> {
    public ListViewModuel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected Observable getObservable() {
        return apiService.getList("1");
    }
}
