package net.csdcodes.model;

public class WeChatData {
    // URLString sentmsgurl = "https://qyapi.weixin.qq.com/cgi-bin/mexin.qq.com/cgi-bin/mexin.qq.com/cgi-bin/Message/send"
    /**
     * Member account
     */
    private String touser;

    /**
     * Message type
     */
    private String msgtype;

    /**
     * Enterprise useful AgentID
     */
    private String agentid;

    /**
     * More than a dozen MAP type data
     */
    private Object text;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public Object getText() {
        return text;
    }

    public void setText(Object text) {
        this.text = text;
    }


}
