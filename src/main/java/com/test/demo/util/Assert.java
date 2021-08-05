package com.test.demo.util;

public class Assert {
    public static void condition(boolean condition,String msg){
        if(condition){
            throw new RuntimeException(msg);
        }
    }
}
