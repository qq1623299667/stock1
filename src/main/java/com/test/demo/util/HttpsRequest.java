package com.test.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpsRequest {
    public static Map<String, String> cookies=new HashMap<>();
    public static Map<String, String> headers = new HashMap<>();
    static {
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.164 Safari/537.36");
    }

    public static void main(String[] args) throws IOException {
        // 登录
//        String url = "http://localhost:8009/login";
//        String requestBody = "{\"userName\":\"admin\",\"password\":\"123456\"}";
//        String login = postLogin(url, requestBody);
//        System.out.println(login);

        String url2 = "https://danjuanapp.com/djapi/fund/nav-growth/001404?day=360";
        String result2 = get(url2);
        log.info(result2);
    }


    // get请求数据
    public static String get(String url1) throws IOException {
        Connection connection = Jsoup.connect(url1).ignoreContentType(true);
        connection.cookies(cookies);
        connection.headers(headers);
        Document objectDoc = connection.get();
        return objectDoc.text();
    }

    // post登录
    public static String postLogin(String url,String requestBody) throws IOException {
        Connection.Response execute = Jsoup.connect(url)
                .requestBody(requestBody)
                .header("Content-Type", "application/json")
                .ignoreContentType(true)
                .method(Connection.Method.POST)
                .execute();
        cookies = execute.cookies();
        return execute.parse().text();
    }

    // post请求数据
    public static String post(String url,String requestBody) throws IOException {
        Connection connection = Jsoup.connect(url)
                .requestBody(requestBody)
                .header("Content-Type", "application/json")
                .ignoreContentType(true)
                .method(Connection.Method.POST);
        connection.cookies(cookies);
        Connection.Response execute = connection.execute();
        return execute.parse().text();
    }
}
