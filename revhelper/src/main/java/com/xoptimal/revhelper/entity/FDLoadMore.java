package com.xoptimal.revhelper.entity;

/**
 * Created by Freddie on 2016/11/14 0014 .
 * Description:
 */
public class FDLoadMore {

    private Status status;

    public FDLoadMore(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public enum Status {
        NORMAL, LOADING, ERROR, MOREOVER, EMPTY
    }


}