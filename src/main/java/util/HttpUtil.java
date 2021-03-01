package util;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Http操作工具类
 */
public class HttpUtil {
    /**
     * 发送POST请求，参数为JSON格式
     *
     * @param url  请求地址
     * @param json JSON格式参数
     * @return response body
     */
    public static String httpPostWithJson(String url, String json) throws Exception {
        String returnValue;
        CloseableHttpResponse resp = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity requestEntity = new StringEntity(json, "utf-8");
            httpPost.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build());
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(requestEntity);
            resp = httpClient.execute(httpPost);
            returnValue = EntityUtils.toString(resp.getEntity());
        } finally {
            if (resp != null) {
                resp.close();
            }
            httpClient.close();
        }
        return returnValue;
    }

    /**
     * 发送POST请求，body参数为JSON格式，自定义添加header参数
     *
     * @param url          请求地址
     * @param json         JSON格式参数
     * @param paramHeadMap header参数
     * @return response body
     */
    public static String httpPostWithJson(String url, String json, Map<String, String> paramHeadMap) throws IOException {
        String returnValue;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse resp = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity requestEntity = new StringEntity(json, "utf-8");

            httpPost.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build());
            httpPost.setHeader("Content-Type", "application/json");
            for (Map.Entry<String, String> entry : paramHeadMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
            httpPost.setEntity(requestEntity);
            resp = httpClient.execute(httpPost);
            returnValue = EntityUtils.toString(resp.getEntity());
        } finally {
            if (resp != null) {
                resp.close();
            }
            httpClient.close();
        }
        return returnValue;
    }

    /**
     * 发送普通get请求
     *
     * @param url 地址
     * @return response body
     */
    public static String httpGet(String url) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } finally {
            if (response != null) {
                response.close();
            }
            client.close();
        }
    }

    /**
     * 发送普通get请求
     *
     * @param url      地址
     * @param paramMap 参数
     * @return response body
     */
    public static String httpGet(String url, Map<String, String> paramMap) throws Exception {
        if (paramMap != null && !paramMap.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
                sb.append("&");
            }
            url = url + "?" + sb.toString();
        }
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } finally {
            if (response != null) {
                response.close();
            }
            client.close();
        }
    }
}
