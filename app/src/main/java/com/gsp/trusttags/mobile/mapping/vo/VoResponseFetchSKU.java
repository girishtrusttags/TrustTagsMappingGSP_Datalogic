package com.gsp.trusttags.mobile.mapping.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class VoResponseFetchSKU implements Serializable {

    private String success = "";
    private String message = "";
    private ArrayList<VoResponseFetchSKUData> data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ArrayList<VoResponseFetchSKUData> getData() {
        return data;
    }

    public void setData(ArrayList<VoResponseFetchSKUData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
