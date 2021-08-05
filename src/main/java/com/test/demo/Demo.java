package com.test.demo;

import com.test.demo.po.Fund2PO;
import com.test.demo.po.FundPO;
import com.test.demo.po.StockPO;
import com.test.demo.po.StockPricePO;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Demo {
    private static final String HIGH = "high";
    private static final String LOW = "low";
    private static final String SPLIT = "_";
    public static void main(String[] args) throws Exception {
        List<String> codes = new ArrayList<>();
        codes.add("166002");
        codes.add("163402");
        codes.add("161005");
        List<StockPO> stockPOList = FundPO.getStockPOList(codes);
        Map<String, Integer>  highestMap= new HashMap<>();
        Map<String, Integer>  lowestMap= new HashMap<>();
        for(int i=0;i<stockPOList.size();i++){
            StockPO stockPO = stockPOList.get(i);
            String name = stockPO.getName();
            for(int j=360;j>30;j = j-30){
                try{
                    boolean highest = StockPricePO.isHighest(name, j);
                    boolean lowest = StockPricePO.isLowest(name, j);
                    if(highest){
                        highestMap.put(name+SPLIT+j,j);
                    }
                    if(lowest){
                        lowestMap.put(name+SPLIT+j,j);
                    }
                }catch (Exception e){
                    if(!(e instanceof RuntimeException)){
                        log.error("{}",e);
                    }
                }
            }
        }

        sendMail(HIGH,highestMap);
        sendMail(LOW,lowestMap);
        Fund2PO.start();
        Thread.sleep(3600_000);
    }

    private static void sendMail(String type,Map<String, Integer> map) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        if(type.equals(HIGH)){
            stringBuilder.append("股票价格过高提醒");
        }else if(type.equals(LOW)){
            stringBuilder.append("股票价格过低提醒");
        }
        if(map.size()<=0){
            log.info("{} 暂无数据",stringBuilder.toString());
            return;
        }
        if(map.size()>0){
            Set<Map.Entry<String, Integer>> entries = map.entrySet();
            entries.forEach(e->{
                String key = e.getKey();
                Integer value = e.getValue();
                stringBuilder.append("\r\n");
                stringBuilder.append(key.split(SPLIT)[0] +"已达到"+value+"天"+getMeaning(type));
            });
        }
        log.info("{}",stringBuilder.toString());
    }

    private static String getMeaning(String type) {
        if(type.equals(LOW)){
            return "最低";
        }else if(type.equals(HIGH)){
            return "最高";
        }
        return "";
    }
}
