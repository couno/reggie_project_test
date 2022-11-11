package com.couno.reggie.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class Rmg<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> Rmg<T> success(T object) {
        Rmg<T> r = new Rmg<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> Rmg<T> error(String msg) {
        Rmg r = new Rmg();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public Rmg<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
