package com.yunda.lib.base_module.db.entity;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by mtt on 2019-11-27
 * Describe
 */
@Dao
public interface UserDao {
    @Insert
    void insert(UserEntity userEntity);


    @Query("SELECT * FROM UserEntity")
    LiveData<List<UserEntity>> getAllByLivedata();

    @Query("SELECT * FROM UserEntity")
    Observable<List<UserEntity>> getAlldata();


}

