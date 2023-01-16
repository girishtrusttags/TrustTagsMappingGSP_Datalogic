package com.gsp.trusttags.mobile.mapping.vo;

import java.io.Serializable;

public class VoResponseForgotPassword implements Serializable {

    private String success = "";
    private String message = "";

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
