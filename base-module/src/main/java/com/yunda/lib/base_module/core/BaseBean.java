package com.yunda.lib.base_module.core;

/**
 * Created by mtt on 2019-11-25
 * Describe
 */
public class BaseBean<T> {
    private int type;
    private T body;
    private Throwable throwable;

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }


    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {

        private String msg;
        private int code = 0;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "body=" + body +
                ", result=" + result +
                '}';
    }
}
