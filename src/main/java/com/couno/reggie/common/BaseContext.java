package com.couno.reggie.common;

/**
 * ClassName: BaseContext
 * date: 2022/11/12 12:11
 *
 * @author Couno
 * ProjectName: reggie
 */

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
