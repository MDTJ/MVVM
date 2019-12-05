package com.yunda.lib.base_module.view.page;

import com.yunda.lib.base_module.view.nopage.NoPageInterface;

/**
 * Created by mtt on 2019-12-03
 * Describe
 */
public interface PageInterface<D> extends NoPageInterface<D> {
    int getTotal();
}
