package com.test.demo.po;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Data
public class StockPO {
    private String name;
    private String code;
//    // 股票代码 000001
//    private String dm;
//    // 股票名称 平安银行
//    private String mc;
    // 股票所属交易所 sz/sh
    private String jys;

    public StockPO(){
    }

    public StockPO(String name){
        this.name = name;
    }

    private static String getStockUrl = "http://ig507.com/data/base/gplist";
    private static Map<String, StockPO> stockNameMap = new HashMap<>();
    private static Map<String, StockPO> stockCodeMap = new HashMap<>();

    public static String getCode(String stockName) {
        Map<String, StockPO> stockMap = getStockNameMap();
        StockPO stockPO = stockMap.get(stockName);
        return stockPO.getJys()+stockPO.getCode();
    }

    private static Map<String, StockPO> getStockNameMap() {
        if(stockNameMap.size()==0){
            queryMap();
        }
        return stockNameMap;
    }

    private static void queryMap() {
        String result= HttpUtil.get(getStockUrl);
        JSONArray jsonArray = JSONUtil.parseArray(result);
        jsonArray.forEach(jsonObject->{
            StockPO stockPO = new StockPO();
//            BeanUtil.copyProperties(jsonObject, StockPO,true);
            String jys = ((JSONObject) jsonObject).getStr("jys");
            String name = ((JSONObject) jsonObject).getStr("mc");
            String code = ((JSONObject) jsonObject).getStr("dm");
            stockPO.setName(name);
            stockPO.setCode(code);
            stockPO.setJys(jys);
            // 补丁：修正部分错误
            stockPO = bugNameFixCommon(stockPO," ","");
            stockPO = bugNameFixCommon(stockPO,"Ａ","A");
            stockNameMap.put(stockPO.getName(),stockPO);
            stockCodeMap.put(stockPO.getJys()+stockPO.getCode(),stockPO);
        });
    }

    private static StockPO bugNameFixCommon(StockPO stockPO, String bugName, String fixName) {
        String mc = stockPO.getName();
        if(mc.contains(bugName)){
            mc = mc.replace(bugName, fixName);
            stockPO.setName(mc);
        }
        return stockPO;
    }

    public String getStockName(String stockCode){
        return stockCodeMap.get(stockCode).getName();
    }
}
