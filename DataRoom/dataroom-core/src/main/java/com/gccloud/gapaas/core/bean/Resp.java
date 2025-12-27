package com.gccloud.gapaas.core.bean;

import lombok.Data;

@Data
public class Resp<T> {
    private Integer code = 500;
    private String message;

    private T data;

    public static <T> Resp<T> success(T data) {
        Resp<T> resp = new Resp<>();
        resp.setCode(200);
        resp.setData(data);
        return resp;
    }

    public static <T> Resp<T> error(Integer code, String message) {
        Resp<T> resp = new Resp<>();
        resp.setCode(code);
        resp.setMessage(message);
        return resp;
    }
}
