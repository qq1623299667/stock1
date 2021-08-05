package com.test.demo.po;

import cn.hutool.http.HttpUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class FundPO {
    private String name;
    private String code;
    private List<StockPO> list;

    public static List<StockPO> getStockPOList(String code){
        String url =  "http://finance.sina.com.cn/fund/quotes/"+code+"/bc.shtml";
        String str = HttpUtil.get(url);
        String url1 = "http://finance.sina.com.cn/realstock/company";
        List<String> list= new ArrayList<>();
        while (str.contains(url1)){
            int index = str.indexOf(url1);
            String substring = str.substring(index);
            int index1 = substring.indexOf(">");
            int index2 = substring.indexOf("<");
            String stockName = substring.substring(index1+1, index2);
            list.add(stockName);
            str = substring.substring(index2);
        }
        List<StockPO> list1 = new ArrayList<>();
        list.forEach(s -> {
            StockPO stockPO = new StockPO(s);
            list1.add(stockPO);
        });
        return list1;
    }

    // 根据基金代码列表获取不重复的股票名称列表
    public static List<StockPO> getStockPOList(List<String> codes){
        List<StockPO> stockPOS = new ArrayList<>();
        for(String fundCode:codes){
            List<StockPO> stockPOList = getStockPOList(fundCode);
            stockPOList.forEach(stockPO->{
                if(!stockPOS.contains(stockPO)){
                    stockPOS.add(stockPO);
                }
            });
        }
        return stockPOS;
    }
}
