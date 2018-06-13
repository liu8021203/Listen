package com.ting.base;

/**
 * Created by liu on 2017/7/6.
 */

public class BaseHttpResult<T> {
    private boolean status;
    private String message;
    private T data;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
