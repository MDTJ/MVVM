package com.mtt.lib.arouter_module;

import androidx.room.Room;

import com.mtt.lib.base_module.db.AppDataBase;

/**
 * Created by mtt on 2019-11-27
 * Describe
 */
public class DBInstance {
    private static final String DB_NAME = "room_test";
    public static AppDataBase appDataBase;
    public static AppDataBase getInstance(){
        if(appDataBase==null){
            synchronized (DBInstance.class){
                if(appDataBase==null){
                    appDataBase = Room.databaseBuilder(BaseApplication.getContext(),AppDataBase.class, DB_NAME)
                            .build();
                }
            }
        }
        return appDataBase;
    }
}
