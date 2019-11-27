package com.yunda.lib.base_module.http;

/**
 * 服务器错误
 * Created by mtt on 2018/5/22.
 */

public class ServerError extends Exception {
    public int code;

    public ServerError(String message, int errorCode) {
        super(message);
        this.code = errorCode;
    }

    @Override
    public String toString() {
        return "code = " + code + "  " + super.toString();
    }
}
