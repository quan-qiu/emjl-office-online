package net.csdcodes.model;

public class WeChatUrlData {

    /**
     * Enterprise ID
     */
    private String corpid;

    /**
     * Secret Management Group's credential key
     */
    private String corpsecret;

    /**
     * Get the request for Token
     */
    private String Get_Token_Url;

    /**
     * Request for sending messages
     */
    private String SendMessage_Url;

    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public String getCorpsecret() {
        return corpsecret;
    }

    public void setCorpsecret(String corpsecret) {
        this.corpsecret = corpsecret;
    }

    public String getGet_Token_Url() {
        return Get_Token_Url;
    }

    public void setGet_Token_Url(String corpid,String corpsecret) {
        Get_Token_Url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpid+"&corpsecret="+corpsecret;
    }

    public String getSendMessage_Url() {
        SendMessage_Url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";
        return SendMessage_Url;
    }

    public void setSendMessage_Url(String sendMessage_Url) {
        SendMessage_Url = sendMessage_Url;
    }
}
