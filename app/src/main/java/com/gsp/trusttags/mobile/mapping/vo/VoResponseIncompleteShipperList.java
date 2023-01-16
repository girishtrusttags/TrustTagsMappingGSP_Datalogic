package com.gsp.trusttags.mobile.mapping.vo;

import java.io.Serializable;

public class VoResponseIncompleteShipperList implements Serializable {

    String id = "";
    String random_id = "";
    String batch_id = "";
    String qrcode_ref_id = "";
    String unique_code = "";
    String is_scan = "";
    String log_id = "";
    String qr_code = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRandom_id() {
        return random_id;
    }

    public void setRandom_id(String random_id) {
        this.random_id = random_id;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getQrcode_ref_id() {
        return qrcode_ref_id;
    }

    public void setQrcode_ref_id(String qrcode_ref_id) {
        this.qrcode_ref_id = qrcode_ref_id;
    }

    public String getUnique_code() {
        return unique_code;
    }

    public void setUnique_code(String unique_code) {
        this.unique_code = unique_code;
    }

    public String getIs_scan() {
        return is_scan;
    }

    public void setIs_scan(String is_scan) {
        this.is_scan = is_scan;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }
}
