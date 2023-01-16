package com.gsp.trusttags.mobile.mapping.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class VoResponceHistory implements Serializable {

    String success = "";
    String message = "";
    String status_code = "";
    ArrayList<VoResponceHistoryListData> history = new ArrayList<>();


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

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public ArrayList<VoResponceHistoryListData> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<VoResponceHistoryListData> history) {
        this.history = history;
    }
}
