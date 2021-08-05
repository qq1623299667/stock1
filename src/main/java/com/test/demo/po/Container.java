package com.test.demo.po;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class Container {
    Map<String, Map<Integer,Boolean>> map = new HashMap<>();

    /**
     * 数据存入
     * @author Will Shi
     * @since 2021/8/5
     */
    public void put(String code, Integer count, Boolean flag){
        Map<Integer, Boolean> map1;
        if(map.containsKey(code)){
            map1 = map.get(code);
        }else {
            map1 = new HashMap<>();
            map.put(code,map1);
        }
        map1.put(count,flag);
    }


    public void printResult(String msg,String noHitMsg){
        boolean flag = false;
        Set<Map.Entry<String, Map<Integer, Boolean>>> entries = map.entrySet();
        for(Map.Entry<String, Map<Integer, Boolean>> entry:entries){
            String key = entry.getKey();
            Map<Integer, Boolean> value = entry.getValue();
            Set<Map.Entry<Integer, Boolean>> entries1 = value.entrySet();
            for(Map.Entry<Integer, Boolean> entry1:entries1){
                Integer key1 = entry1.getKey();
                Boolean value1 = entry1.getValue();
                if(value1){
                    flag = true;
                    log.info(msg,key,key1);
                }
            }
        }
        if(!flag){
            log.info(noHitMsg);
        }
    }
}
