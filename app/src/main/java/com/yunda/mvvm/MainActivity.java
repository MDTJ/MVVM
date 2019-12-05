package com.yunda.mvvm;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.yunda.annotation.SingleClick;
import com.yunda.lib.base_module.db.entity.UserEntity;
import com.yunda.lib.base_module.mvvm.BaseMVVMActivity;
import com.yunda.mvvm.databinding.ActivityMainBinding;

import java.util.List;


public class MainActivity extends BaseMVVMActivity<MainViewModel, ActivityMainBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void setListener() {
        viewDataBinding.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observe(viewModel.getData(),  new OnCallBack<UserBean>() {
                    @Override
                    public void onSuccess(UserBean body) {
                        viewDataBinding.setUser(body);
                    }
                });

            }
        });
    }

    @Override
    public void initData() {
        setToolBarTitle("Main");
        initEmptyViewLayout(R.id.empty);
        ListPageFragment listPageFragment = new ListPageFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment,listPageFragment).commit();

    }

    @SingleClick(1000)
    public void insert(View view) {
        UserEntity userEntity=new UserEntity();
        userEntity.setName("mtt");
        userEntity.setAddress("aaaaa");
        viewModel.insert(userEntity);

    }

    public void query(View view) {

        final LiveData<List<UserEntity>> query = viewModel.query();
        query.observe(this, new Observer<List<UserEntity>>() {
            @Override
            public void onChanged(List<UserEntity> userEntities) {
                query.removeObservers(MainActivity.this);
            }
        });
    }

}
