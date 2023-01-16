package com.gsp.trusttags.mobile.mapping.vo;

import java.io.Serializable;

public class VoResponseScanSecondLevel implements Serializable {

    String success = "";
    String message = "";
    boolean isUpdated = false;

    VoResponceScanShipperCodeData data;

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

    public VoResponceScanShipperCodeData getData() {
        return data;
    }

    public void setData(VoResponceScanShipperCodeData data) {
        this.data = data;
    }

    public class VoResponceScanShipperCodeData implements Serializable {
        String id = "";

        public boolean isIs_scan() {
            return is_scan;
        }

        public void setIs_scan(boolean is_scan) {
            this.is_scan = is_scan;
        }

        boolean is_scan = false;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


    }

}
