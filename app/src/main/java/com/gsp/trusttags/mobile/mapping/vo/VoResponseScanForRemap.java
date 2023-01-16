package com.gsp.trusttags.mobile.mapping.vo;

import java.io.Serializable;

public class VoResponseScanForRemap implements Serializable {

    private String success = "";
    private String message = "";
    private String status_code = "";
    private VoResponseScanForRemapData data;

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

    public VoResponseScanForRemapData getData() {
        return data;
    }

    public void setData(VoResponseScanForRemapData data) {
        this.data = data;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }
}
