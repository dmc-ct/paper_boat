package com.paper.boat.zrdx.network;


public class Result<T> {
    public static final int SUCCESSED = 0;

    public int code;
    public String message;
    public T data;
    public int total;
}
