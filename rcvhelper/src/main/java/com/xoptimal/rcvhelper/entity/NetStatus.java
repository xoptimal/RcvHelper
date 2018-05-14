package com.xoptimal.rcvhelper.entity;

/**
 * Created by Freddie on 2016/11/14 0014 .
 * Description:
 */
public class NetStatus {

    private Status status;

    public NetStatus(Status status) {
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