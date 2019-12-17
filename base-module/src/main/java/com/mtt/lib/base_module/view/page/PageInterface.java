package com.mtt.lib.base_module.view.page;

import com.mtt.lib.base_module.view.nopage.NoPageInterface;

/**
 * Created by mtt on 2019-12-03
 * Describe
 */
public interface PageInterface<D> extends NoPageInterface<D> {
    int getTotal();
}
