package com.yunda.mvvm;

import androidx.lifecycle.LiveData;

import com.yunda.lib.arouter_module.DBInstance;
import com.yunda.lib.base_module.db.entity.UserEntity;
import com.yunda.lib.base_module.mvvm.BaseRepository;

import java.util.List;

/**
 * Created by mtt on 2019-11-28
 * Describe
 */
public class MainRepository extends BaseRepository<MainApiService> {

    public LiveData<List<UserEntity>> getNativeData(){
        return DBInstance.getInstance().getUserDao().getAllByLivedata();
    }

    public void insert(UserEntity entity){
        DBInstance.getInstance().getUserDao().insert(entity);
    }

}
