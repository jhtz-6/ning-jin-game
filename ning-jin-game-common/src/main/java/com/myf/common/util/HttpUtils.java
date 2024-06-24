package com.myf.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.Map;

/**
 * @Author: myf
 * @CreateTime: 2024-05-01  17:33
 * @Description: TODO
 */
@Slf4j
public class HttpUtils {



    public static String sendPostWithJson(String url, String json){
        String result = "";
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            BasicResponseHandler handler = new BasicResponseHandler();
            //解决中文乱码问题
            StringEntity entity = new StringEntity(json, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            result = httpClient.execute(httpPost, handler);
        } catch (Exception e) {
            log.error("sendPostWithJson.error.url:{},json:{}",url,json,e);
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                log.error("sendPostWithJson.close.error.url:{},json:{}",url,json,e);
            }
        }
        return result;
    }

    public static String sendGetWithMap(String url, Map<String,String> headerMap){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            BasicResponseHandler handler = new BasicResponseHandler();
            //解决中文乱码问题
            if(MapUtils.isNotEmpty(headerMap)){
                headerMap.forEach(uriBuilder::addParameter);
            }
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            return httpClient.execute(httpGet, handler);
        } catch (Exception e) {
            log.error("sendGetWithMap.error.url:{},headerMap:{}",url,headerMap,e);
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                log.error("sendGetWithMap.close.error.url:{},headerMap:{}",url,headerMap,e);
            }
        }
        return null;
    }
}
