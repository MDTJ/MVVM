package com.yunda.mvvm.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by mtt on 2019-11-27
 * Describe
 */
@Dao
public interface UserDao {
    @Insert
    Long insert(UserEntity userEntity);


    @Delete
    void delete(UserEntity userEntity);


    @Update()
    void update(UserEntity userEntity);


    @Query("SELECT * FROM UserEntity")
    LiveData<List<UserEntity>> getAllByLivedata();

    @Query("SELECT * FROM UserEntity")
    Flowable<List<UserEntity>> getByUid();


    @Query("SELECT * FROM UserEntity")
    Observable<List<UserEntity>> getByUid2();



}

