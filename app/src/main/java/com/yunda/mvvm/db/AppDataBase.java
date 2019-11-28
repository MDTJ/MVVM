package com.yunda.mvvm.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;


/**
 * Created by mtt on 2019-11-27
 * Describe
 */
@Database(entities = {UserEntity.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract UserDao getUserDao();
}
