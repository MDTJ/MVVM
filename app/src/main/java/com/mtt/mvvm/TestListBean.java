package com.mtt.mvvm;


import com.mtt.lib.base_module.view.page.PageInterface;

import java.util.List;

/**
 * Created by mtt on 2019-11-18
 * Describe
 */
public class TestListBean implements PageInterface<TestListBean.Bean> {

    private List<Bean> beans;
    private int totalCount;

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<Bean> getBeans() {
        return beans;
    }

    @Override
    public int getTotal() {
        return totalCount;
    }

    public void setBeans(List<Bean> beans) {
        this.beans = beans;
    }

    public int getTotalCount() {
        return totalCount;
    }

    @Override
    public List<Bean> getList() {
        return beans;
    }

    public static class Bean {

        private String title;

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}
