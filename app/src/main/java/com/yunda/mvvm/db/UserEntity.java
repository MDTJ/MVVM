package com.yunda.mvvm.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by mtt on 2019-11-27
 * Describe
 */
@Entity
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    private String name;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

