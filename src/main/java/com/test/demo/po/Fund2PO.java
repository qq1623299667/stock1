package com.test.demo.po;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.test.demo.util.HttpsRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检测
 * @author Will Shi
 * @since 2021/8/5
 */
@Slf4j
@Data
public class Fund2PO {
    public static Map<String,List<Fund2PO>> map = new HashMap<>();

    private String date;
    private String gr_nav;
    private String gr_per;
    private String hs300_nav;

    public static void start() throws IOException {
        List<String> codes = new ArrayList<>();
        codes.add("166002");
        codes.add("163402");
        codes.add("161005");
        Container highestContainer = new Container();
        Container lowestContainer = new Container();
        for(int i=0;i<codes.size();i++){
            for(int j=30;j<100;j=j+30){
                boolean highest = isHighest(codes.get(i), j);
                highestContainer.put(codes.get(i),j,highest);

                boolean lowest = isLowest(codes.get(i), j);
                lowestContainer.put(codes.get(i),j,lowest);
            }
        }

        highestContainer.printResult("{} 已达到 {} 天最高","没有出现最高的");
        lowestContainer.printResult("{} 已达到 {} 天最低","没有出现最低的");
    }


    /**
     * 是否是n天最低
     * @author Will Shi
     * @since 2021/8/5
     */
    public static boolean isLowest(String code,Integer count) throws IOException {

        List<Fund2PO> list = getList(code);
        Fund2PO latest = list.get(list.size() - 1);
        String gr_nav1 = latest.gr_nav;
        BigDecimal grNavB1 = new BigDecimal(gr_nav1);
        for(int i=list.size()-count;i<list.size();i++){
            Fund2PO fund2PO = list.get(i);
            String grNav = fund2PO.gr_nav;
            BigDecimal grNavB = new BigDecimal(grNav);
            if(grNavB1.compareTo(grNavB)>=0){
                return false;
            }
        }
        return true;
    }

    /**
     * 获取列表
     * @author Will Shi
     * @since 2021/8/5
     */
    private static List<Fund2PO> getList(String code) throws IOException {
        if(map.containsKey(code)){
            return map.get(code);
        }
        String url2 = "https://danjuanapp.com/djapi/fund/nav-growth/"+code+"?day=360";
        String result2 = HttpsRequest.get(url2);
        log.debug(result2);
        JSONObject parse = (JSONObject) JSON.parse(result2);
        JSONObject data = parse.getJSONObject("data");
        JSONArray fundNavGrowth = (JSONArray) data.get("fund_nav_growth");
        List<Fund2PO> fund2POS = new ArrayList<>();
        for(int i=0;i<fundNavGrowth.size();i++){
            JSONObject jsonObject = (JSONObject) fundNavGrowth.get(i);
            Fund2PO fund2PO = JSON.parseObject(jsonObject.toJSONString(), Fund2PO.class);
            fund2POS.add(fund2PO);
        }
        map.put(code,fund2POS);
        return fund2POS;
    }

    /**
     * 是否是n天最高
     * @author Will Shi
     * @since 2021/8/5
     */
    public static boolean isHighest(String code,Integer count) throws IOException {
        List<Fund2PO> list = getList(code);
        Fund2PO latest = list.get(list.size() - 1);
        String gr_nav1 = latest.gr_nav;
        BigDecimal grNavB1 = new BigDecimal(gr_nav1);
        for(int i=list.size()-count;i<list.size();i++){
            Fund2PO fund2PO = list.get(i);
            String grNav = fund2PO.gr_nav;
            BigDecimal grNavB = new BigDecimal(grNav);
            if(grNavB1.compareTo(grNavB)<=0){
                return false;
            }
        }
        return true;
    }
}
