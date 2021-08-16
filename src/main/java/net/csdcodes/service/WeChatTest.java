package net.csdcodes.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.csdcodes.model.WeChatUrlData;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.*;

public class WeChatTest {

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    public static void main(String[] args) throws Exception{
        WeChatTest weChatTest = new WeChatTest();
        try {
            System.out.println("Testing 1 - Send Http GET request");
            weChatTest.sendGet();

            System.out.println("Testing 2 - Send Http POST request");
            //weChatTest.sendGet();
        } finally {
            weChatTest.close();
        }
    }

    private void close() throws IOException {
        httpClient.close();
    }

    private void sendGet() throws Exception {

        HttpGet request = new HttpGet("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ww4f77ff551a51c6cc&corpsecret=Mw5otFib2Qa_RdSOmP_ri6yHqBKQtJJ8ULMoeIpZytA");

        // add request headers
        //request.addHeader("custom-key", "mkyong");
        //request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                JsonObject data = new Gson().fromJson( entity.toString(), JsonObject.class);
                // return it as a String
                //String result = EntityUtils.toString(entity);
                //System.out.println("result : " + result);
                System.out.println(data.get("access_token"));
            }

        }

    }

}

