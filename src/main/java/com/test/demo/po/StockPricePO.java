package com.test.demo.po;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RuntimeUtil;
import com.test.demo.util.Assert;
import com.test.demo.util.HttpsUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class StockPricePO {
    private StockPO stockPO;
    private String date;
    private BigDecimal openPrice;
    private BigDecimal closingPrice;
    private BigDecimal highestPrice;
    private BigDecimal lowestPrice;


    // 获取多个股票日k
    public Map<String,List<StockPricePO>> getMulti(List<String> stockNames) {
        Map<String,List<StockPricePO>> map = new HashMap<>();
        stockNames.forEach(stockName->{
            // 获取股票代码
            List<StockPricePO> list1 = get(stockName);
            map.put(stockName,list1);
        });
        return map;
    }

    // 获取股票日k
    public static List<StockPricePO> get(String stockName) {
        List<StockPricePO> list = new ArrayList<>();
        String code = StockPO.getCode(stockName);
        StockPO stockPO = new StockPO(stockName);
        String url = "https://data.gtimg.cn/flashdata/hushen/latest/daily/"+code+".js?maxage=43201";
        String resultJson = HttpsUtil.httpsRequest(url,"GET",null);
        String[] split = resultJson.replace("\";","").split("\\\\n\\\\");
        if(split.length>2){
            for(int i=2;i<split.length;i++){
                StockPricePO stockPricePO = packageToStockPricePO(split[i],stockPO);
                list.add(stockPricePO);
            }
        }
        return list;
    }

    private static StockPricePO packageToStockPricePO(String str, StockPO stockPO) {
        StockPricePO stockPricePO = new StockPricePO();
        String[] split = str.split(" ");
        stockPricePO.setDate(split[0]);
        stockPricePO.setOpenPrice(new BigDecimal(split[1]));
        stockPricePO.setClosingPrice(new BigDecimal(split[2]));
        stockPricePO.setHighestPrice(new BigDecimal(split[3]));
        stockPricePO.setLowestPrice(new BigDecimal(split[4]));
        stockPricePO.setStockPO(stockPO);
        return stockPricePO;
    }

    // 判断最新的股票是否是历史最低/最高，2个月，3个月等等
    public static boolean isLowest(String name,Integer count){
        List<StockPricePO> stockPricePOS = get(name);
        StockPricePO latestStockPrice = stockPricePOS.get(stockPricePOS.size() - 1);
        boolean isLowest = true;
        for(int i=stockPricePOS.size()-count-1;i<stockPricePOS.size();i++){
            if(stockPricePOS.get(i).getLowestPrice().compareTo(latestStockPrice.getLowestPrice())<0){
                isLowest = false;
                break;
            }
        }
        return isLowest;
    }

    // 判断最新的股票是否是历史最低/最高，2个月，3个月等等
    public static boolean isHighest(String name,Integer count){
        List<StockPricePO> stockPricePOS = get(name);
        Assert.condition(stockPricePOS.size()<count,"当前展示股票天数不足"+count+"天");
        StockPricePO latestStockPrice = stockPricePOS.get(stockPricePOS.size() - 1);
        boolean isHighest = true;
        for(int i=stockPricePOS.size()-count-1;i<stockPricePOS.size();i++){
            if(stockPricePOS.get(i).getLowestPrice().compareTo(latestStockPrice.getLowestPrice())>0){
                isHighest = false;
                break;
            }
        }
        return isHighest;
    }
}
