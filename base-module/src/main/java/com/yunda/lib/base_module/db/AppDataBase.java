package com.yunda.lib.base_module.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.yunda.lib.base_module.db.entity.UserDao;
import com.yunda.lib.base_module.db.entity.UserEntity;


/**
 * Created by mtt on 2019-11-27
 * Describe
 */
@Database(entities = {UserEntity.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract UserDao getUserDao();
}
