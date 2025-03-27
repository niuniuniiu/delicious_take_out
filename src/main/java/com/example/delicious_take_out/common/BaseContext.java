package com.example.delicious_take_out.common;

public class BaseContext {
    private  static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();


    /*设置值*/
    public static void setCurrentId(Integer id) {
        threadLocal.set(id);
    }

    /*获取值*/
    public static Integer getCurrentId() {
        return threadLocal.get();
    }


}
