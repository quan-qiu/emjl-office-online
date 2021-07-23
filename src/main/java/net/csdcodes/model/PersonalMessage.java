package net.csdcodes.model;

public class PersonalMessage {
    private String toUser;

    private String msgType;

    private String agentId;

    private String text;

    private int safe = 0;
    private int enable_id_trans =  0;
    private int enable_duplicate_check =  0;
    private int duplicate_check_interval = 1800;

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSafe() {
        return safe;
    }

    public void setSafe(int safe) {
        this.safe = safe;
    }

    public int getEnable_id_trans() {
        return enable_id_trans;
    }

    public void setEnable_id_trans(int enable_id_trans) {
        this.enable_id_trans = enable_id_trans;
    }

    public int getEnable_duplicate_check() {
        return enable_duplicate_check;
    }

    public void setEnable_duplicate_check(int enable_duplicate_check) {
        this.enable_duplicate_check = enable_duplicate_check;
    }

    public int getDuplicate_check_interval() {
        return duplicate_check_interval;
    }

    public void setDuplicate_check_interval(int duplicate_check_interval) {
        this.duplicate_check_interval = duplicate_check_interval;
    }

    @Override
    public String toString() {
        return "PersonalMessage{" +
                "toUser='" + toUser + '\'' +
                ", msgType='" + msgType + '\'' +
                ", agentId='" + agentId + '\'' +
                ", text='" + text + '\'' +
                ", safe=" + safe +
                ", enable_id_trans=" + enable_id_trans +
                ", enable_duplicate_check=" + enable_duplicate_check +
                ", duplicate_check_interval=" + duplicate_check_interval +
                '}';
    }
}
