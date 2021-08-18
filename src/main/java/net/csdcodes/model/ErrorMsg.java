package net.csdcodes.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMsg {

    @JsonProperty("code")
    private int code;

    @JsonProperty("message")
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
