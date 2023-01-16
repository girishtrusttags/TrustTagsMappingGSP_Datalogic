package com.gsp.trusttags.mobile.mapping.vo;

import java.io.Serializable;

public class VoResponceScanPackageQrCode implements Serializable {

    private String success = "";
    private String message = "";
    private String qrcode = "";
    private String sub_code = "";
    private boolean isUpdated = false;

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

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }
}
