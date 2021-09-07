package net.csdcodes.service;

import net.csdcodes.model.WeChatData;
import net.csdcodes.model.WeChatUrlData;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class WeChatMsgSendService {



    /**
     * Used to submit login data
     */
    private HttpPost httpPost;

    /**
     * Used to get the login page
     */
    private HttpGet httpGet;

    public static final String CONTENT_TYPE = "Content-Type";

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Gson gson = new Gson();

    /**
     * WeChat authorization request, get type, get an authorized response, used for other methods to intercept TOKEN
     *
     * @param Get_Token_Url
     * @Return String Authorized Response Content
     * @throws IOException
     */
    protected String toAuth(String Get_Token_Url) throws IOException {
        //httpClient = HttpClients.createDefault();
        //HttpClient httpclient = new DefaultHttpClient();
        CloseableHttpClient httpClient = HttpClients.createDefault();

        System.out.println(Get_Token_Url);
        httpGet = new HttpGet(Get_Token_Url);
        //httpGet.setHeader("Content-Type", "text/plain; charset=utf-8");
        //httpGet.setHeader("Expect", "100-continue");
        CloseableHttpResponse response = null;
        String resp = "";

        try {
            response = httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            resp = EntityUtils.toString(entity, "utf-8");
            //System.out.println(resp);
            EntityUtils.consume(entity);
        } catch (ClientProtocolException e) {
            //Log.e(getClass().getSimpleName(), "HTTP protocol error", e);
            System.out.println("HTTP protocol error: " + e);
        } catch (IOException e) {
           // Log.e(getClass().getSimpleName(), "Communication error", e);
            System.out.println("Communication error: " + e);
        } finally {
            response.close();
            //response.getEntity().consumeContent();
        }
        LoggerFactory.getLogger(getClass()).info(" resp:{}", resp);
        return resp;
    }

    /**
     * Corpid Application Organization Number Corpsecret Application Key Get ToAuth (String
     * Get_token_URL) Returns the value of the   key value to the Access_Token key
     *
     * @param
     */
    public String getToken(String corpid, String corpsecret) throws IOException {
        WeChatMsgSendService sw = new WeChatMsgSendService();
        WeChatUrlData uData = new WeChatUrlData();
        uData.setGet_Token_Url(corpid, corpsecret);
        String resp = sw.toAuth(uData.getGet_Token_Url());
        System.out.println("resp=====:" + resp);
        try {
            Map<String, Object> map = gson.fromJson(resp, new TypeToken<Map<String, Object>>() {
            }.getType());
            return map.get("access_token").toString();
        } catch (Exception e) {
            e.getStackTrace();
            return resp;
        }
    }

    /**
     * Create a WeChat Send Request Post Data Touser Send Message Receiver, MSGType Message Type (Text / Picture, etc.), Application_ID Application Number.
     * This method applies to Text type WeChat messages, ContentKey and ContentValue can only group a pair.
     *
     * @param touser
     * @param msgtype
     * @param application_id
     * @param contentKey
     * @param contentValue
     * @return
     */
    public String createpostdata(String touser, String msgtype, int application_id, String contentKey,
                                 String contentValue) {
        WeChatData wcd = new WeChatData();
        wcd.setTouser(touser);
        wcd.setAgentid(application_id + "");
        wcd.setMsgtype(msgtype);
        Map<Object, Object> content = new HashMap<Object, Object>();
        content.put(contentKey, contentValue);
        wcd.setText(content);
        return gson.toJson(wcd);
    }

    /**
     * @Title Creating WeChat Send Request Post Entity, Charset Message Coding, ContentType Message Content Type,
     * URL WeChat message sends a request address, Data is Post data, token authentication token
     * @param charset
     * @param contentType
     * @param url
     * @param data
     * @param token
     * @return
     * @throws IOException
     */
    public String post(String charset, String contentType, String url, String data, String token) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        httpPost = new HttpPost(url + token);
        httpPost.setHeader(CONTENT_TYPE, contentType);
        httpPost.setEntity(new StringEntity(data, charset));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        String resp;
        try {
            HttpEntity entity = response.getEntity();
            resp = EntityUtils.toString(entity, charset);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        LoggerFactory.getLogger(getClass()).info("call [{}], param:{}, resp:{}", url, data, resp);
        return resp;
    }



}
