package net.csdcodes.component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.csdcodes.model.PersonalMessage;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


public class WechatApiComponent {

    protected static String PREFIX_URL        = "https://qyapi.weixin.qq.com";


    protected static String USER_CREATE       = "/cgi-bin/user/create?access_token=";
    protected static String USER_GET          = "/cgi-bin/user/get?access_token=";
    protected static String USER_UPDATE       = "/cgi-bin/user/update?access_token=";
    protected static String USER_DELETE       = "/cgi-bin/user/delete?access_token=";
    protected static String USER_BATCH_DELETE = "/cgi-bin/user/batchdelete?access_token=";
    //https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD(1-递归获取，0-只获取本部门)
    protected static String USER_SIMPLE_LIST  = "/cgi-bin/user/simplelist?access_token=";
    //https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD
    protected static String USER_LIST         = "/cgi-bin/user/list?access_token=";
    protected static String USERID_TO_OPENID  = "/cgi-bin/user/convert_to_openid?access_token=";
    protected static String OPENID_TO_USERID  = "/cgi-bin/user/convert_to_userid?access_token=";
    protected static String USER_AUTH_SUCCESS = "/cgi-bin/user/authsucc?access_token=";

    protected static String DEPARTMENT_CREATE = "/cgi-bin/department/create?access_token=";
    protected static String DEPARTMENT_UPDATE = "/cgi-bin/department/update?access_token=";
    protected static String DEPARTMENT_DELETE = "/cgi-bin/department/delete?access_token=";
    protected static String DEPARTMENT_LIST   = "/cgi-bin/department/list?access_token=";

    protected static String TAG_CREATE        = "/cgi-bin/tag/create?access_token=";
    protected static String TAG_UPDATE        = "/cgi-bin/tag/update?access_token=";
    protected static String TAG_DELETE        = "/cgi-bin/tag/delete?access_token=";
    protected static String TAG_GET_USER      = "/cgi-bin/tag/get?access_token=";
    protected static String TAG_ADD_USER      = "/cgi-bin/tag/addtagusers?access_token=";
    protected static String TAG_DELETE_USER   = "/cgi-bin/tag/deltagusers?access_token=";
    protected static String TAG_GET_LIST      = "/cgi-bin/tag/list?access_token=";

    protected static String BATCH_JOB_GET_RESULT = "/cgi-bin/batch/getresult?access_token=";

    protected static String BATCH_INVITE      = "/cgi-bin/batch/invite?access_token=";

    protected static String AGENT_GET         = "/cgi-bin/agent/get?access_token=";
    protected static String AGENT_SET         = "/cgi-bin/agent/set?access_token=";
    protected static String AGENT_GET_LIST    = "/cgi-bin/agent/list?access_token=";

    protected static String MENU_CREATE       = "/cgi-bin/menu/create?access_token=";
    protected static String MENU_GET          = "/cgi-bin/menu/get?access_token=";
    protected static String MENU_DELETE       = "/cgi-bin/menu/delete?access_token=";

    protected static String MESSAGE_SEND      = "/cgi-bin/message/send?access_token=";

    protected static String MEDIA_GET         = "/cgi-bin/media/get?access_token=";

    protected static String GET_USER_INFO_BY_CODE = "/cgi-bin/user/getuserinfo?access_token=";
    protected static String GET_USER_DETAIL   = "/cgi-bin/user/getuserdetail?access_token=";

    protected static String GET_TICKET        = "/cgi-bin/ticket/get?access_token=";
    protected static String GET_JSAPI_TICKET  = "/cgi-bin/get_jsapi_ticket?access_token=";

    protected static String GET_CHECKIN_OPTION = "/cgi-bin/checkin/getcheckinoption?access_token=";
    protected static String GET_CHECKIN_DATA  = "/cgi-bin/checkin/getcheckindata?access_token=";
    protected static String GET_APPROVAL_DATA = "/cgi-bin/corp/getapprovaldata?access_token=";

    protected static String GET_INVOICE_INFO  = "/cgi-bin/card/invoice/reimburse/getinvoiceinfo?access_token=";
    protected static String UPDATE_INVOICE_STATUS = "/cgi-bin/card/invoice/reimburse/updateinvoicestatus?access_token=";
    protected static String BATCH_UPDATE_INVOICE_STATUS = "/cgi-bin/card/invoice/reimburse/updatestatusbatch?access_token=";
    protected static String BATCH_GET_INVOICE_INFO = "/cgi-bin/card/invoice/reimburse/getinvoiceinfobatch?access_token=";

    protected static String GET_PRE_AUTH_CODE = "/cgi-bin/service/get_pre_auth_code?suite_access_token=SUITE_ACCESS_TOKEN";
    protected static String SET_SESSION_INFO  = "/cgi-bin/service/set_session_info?suite_access_token=SUITE_ACCESS_TOKEN";
    protected static String GET_PERMANENT_CODE = "/cgi-bin/service/get_permanent_code?suite_access_token=SUITE_ACCESS_TOKEN";
    protected static String GET_AUTH_INFO     = "/cgi-bin/service/get_auth_info?suite_access_token=SUITE_ACCESS_TOKEN";
    protected static String GET_ADMIN_LIST    = "/cgi-bin/service/get_admin_list?suite_access_token=SUITE_ACCESS_TOKEN";
    protected static String GET_USER_INFO_BY_3RD = "/cgi-bin/service/getuserinfo3rd?suite_access_token=SUITE_ACCESS_TOKEN";
    protected static String GET_USER_DETAIL_BY_3RD = "/cgi-bin/service/getuserdetail3rd?suite_access_token=SUITE_ACCESS_TOKEN";

    protected static String GET_LOGIN_INFO    = "/cgi-bin/service/get_login_info?access_token=PROVIDER_ACCESS_TOKEN";
    protected static String GET_REGISTER_CODE = "/cgi-bin/service/get_register_code?provider_access_token=PROVIDER_ACCESS_TOKEN";
    protected static String GET_REGISTER_INFO = "/cgi-bin/service/get_register_info?provider_access_token=PROVIDER_ACCESS_TOKEN";
    protected static String SET_AGENT_SCOPE   = "/cgi-bin/agent/set_scope";
    protected static String SET_CONTACT_SYNC_SUCCESS = "/cgi-bin/sync/contact_sync_success";
   // --------------------------

    protected final static String CORPID            = "ww4f77ff551a51c6cc";

    protected final static String AGENTID           = "1000016";

    protected final static String BUSINESS          = "java-pay";

    protected final static String CORPSECRET        ="Mw5otFib2Qa_RdSOmP_ri6yHqBKQtJJ8ULMoeIpZytA";

    //For example: https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ID&corpsecret=SECRET
    protected static String ACCESS_TOKEN_URL  = PREFIX_URL + "/cgi-bin/gettoken?corpid=" + CORPID + "&corpsecret=" + CORPSECRET;

    protected String access_token;

    protected int expires_in = 7200;

    public String getAGENTID(){
        return AGENTID;
    }

    public void setAccessToken(){

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(ACCESS_TOKEN_URL, String.class);
        JsonObject data = new Gson().fromJson(result, JsonObject.class);

        if (data.get("errcode").getAsInt() == 0){
            this.access_token = data.get("access_token").getAsString();
        }else{
            this.access_token = null;
        }

    }

    public String getAccessToken(){
        return this.access_token;
    }


    public String sendMesToPersonal(JsonObject personMessage){
        RestTemplate restTemplate = new RestTemplate();

        //JsonObject request = buildMessageBody(personMessage);
        String s_request = new Gson().toJson(personMessage);

        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + access_token;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<String>(s_request, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        HttpStatus statusCode = responseEntity.getStatusCode();
        System.out.println("send message Status code - " + statusCode);
        String body = responseEntity.getBody();

        System.out.println("send message response body - " + body);


        return body;
    }

    public JsonObject buildMessageBody(PersonalMessage personalMessage){

        JsonObject root = new JsonObject();
        root.addProperty("touser", personalMessage.getToUser());
        root.addProperty("msgtype", personalMessage.getMsgType());
        root.addProperty("agentid", personalMessage.getAgentId());
        root.addProperty("safe", personalMessage.getSafe());
        root.addProperty("enable_id_trans", personalMessage.getEnable_id_trans());
        root.addProperty("enable_duplicate_check", personalMessage.getEnable_duplicate_check());
        root.addProperty("duplicate_check_interval", personalMessage.getDuplicate_check_interval());
        JsonObject content = new JsonObject();
        content.addProperty("content","this is a test");

        root.add("text", content);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        System.out.println(gson.toJson(root));
        return root;

    }

}
