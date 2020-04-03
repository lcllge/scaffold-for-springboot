package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @version V1.0.0
 * @ClassName: {@link HttpUtils}
 * @Description: Http协议远程调用工具
 * @author: 兰州
 * @date: 2019/7/16 10:12
 * @Copyright:2019 All rights reserved.
 */
@Slf4j
public class HttpUtils {

    /**
     * 请求超时时间,这个时间定义了socket读数据的超时时间，
     * 也就是连接到服务器之后到从服务器获取响应数据需要等待的时间,发生超时，
     * 会抛出SocketTimeoutException异常。
     */
    private static final int SOCKET_TIME_OUT = 60000;
    /**
     * 连接超时时间,这个时间定义了通过网络与服务器建立连接的超时时间，
     * 也就是取得了连接池中的某个连接之后到接通目标url的连接等待时间。
     * 发生超时，会抛出ConnectionTimeoutException异常
     */
    private static final int CONNECT_TIME_OUT = 60000;

    /**
     * 下载调用
     *
     * @param url
     * @param paramMap
     * @param filName
     * @param httpServletResponse
     */
    public static void downloadMethod(String url, Map<String, String> paramMap, String filName, HttpServletResponse httpServletResponse) {
        log.info("开始下载文件");
        log.debug("参数 {},{},{}", url, paramMap, filName);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        URI uri = null;
        List<NameValuePair> params = new ArrayList<>();
        paramMap.forEach((k, v) -> {
            params.add(new BasicNameValuePair(k, v));
        });
        try {
            uri = (new URIBuilder(url)).setParameters(params).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000).setSocketTimeout(5000).setRedirectsEnabled(true).build();
            httpGet.setConfig(requestConfig);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    InputStream inputStream = responseEntity.getContent();
                    httpServletResponse.setContentType("text/html; charset=UTF-8");
                    httpServletResponse.setContentType("application/x-msdownload");
                    httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filName, "utf-8"));
                    ServletOutputStream out = httpServletResponse.getOutputStream();
                    int data;
                    while ((data = inputStream.read()) != -1) {
                        out.write(data);
                    }
                    out.flush();
                    out.close();
                }
            } else if (response.getStatusLine().getStatusCode() == 404) {
                httpServletResponse.setContentType("text/html; charset=UTF-8");
                HttpEntity responseEntity = response.getEntity();
                httpServletResponse.getWriter().print(EntityUtils.toString(responseEntity));
                httpServletResponse.getWriter().close();
            }
        } catch (IOException e) {
            log.error("下载异常", e);
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("关闭异常", e);
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("关闭异常", e);
                }
            }

        }

    }

    /**
     * GET 远程调用
     *
     * @param url
     * @param paramMap
     * @return
     */
    public static String getMethod(String url, Map<String, String> paramMap) {
        log.info("执行get远程调用方法");
        log.debug("参数 {},{}", url, paramMap);
        String result = null;
        // 1、创建httpClient
        CloseableHttpClient client = HttpClients.createDefault();
        // 2、封装请求参数
        List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        //3、转化参数
        String params = null;
        CloseableHttpResponse response = null;
        try {
            params = EntityUtils.toString(new UrlEncodedFormEntity(list, Consts.UTF_8));
            //4、创建HttpGet请求
            HttpGet httpGet = new HttpGet(url + "?" + params);
            response = client.execute(httpGet);
            //5、获取实体
            HttpEntity entity = response.getEntity();
            //将实体装成字符串
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
            log.error("参数转换出错! {}", e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                log.error("参数流关闭出错! {}", e);
            }
        }
        return result;
    }

    /**
     * POST 远程调用
     *
     * @param url
     * @param paramMap
     * @return
     */
    public static String postMethod(String url, Map<String, String> paramMap) {
        log.info("执行post远程调用方法");
        log.debug("参数 {},{}", url, paramMap);
        String result = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        List<NameValuePair> params = new ArrayList<>();
        paramMap.forEach((k, v) -> {
            params.add(new BasicNameValuePair(k, v));
        });
        HttpEntity entity = new UrlEncodedFormEntity(params, Consts.UTF_8);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000).setSocketTimeout(5000).setRedirectsEnabled(true).build();
            httpPost.setConfig(requestConfig);
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            result = EntityUtils.toString(responseEntity);
        } catch (IOException e) {
            log.error("执行异常", e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("关闭异常", e);
            }
        }
        return result;
    }


}
