package net.csdcodes.model;

import org.springframework.http.HttpStatus;

public class EmailResponse {
    private String result;
    private String status;
    private HttpStatus msg;

    public EmailResponse(String result, String status, HttpStatus msg) {
        this.result = result;
        this.status = status;
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HttpStatus getMsg() {
        return msg;
    }

    public void setMsg(HttpStatus msg) {
        this.msg = msg;
    }
}
