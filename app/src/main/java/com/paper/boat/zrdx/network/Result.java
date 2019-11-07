package com.paper.boat.zrdx.network;

/*根据返回格式可以自定义*/
public class Result<T> {
    static final int SUCCESSED = 0;

    public int code;
    public String message;
    public T data;
    public int total;
}
