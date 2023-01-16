package com.gsp.trusttags.mobile.mapping.vo;

import java.io.Serializable;

public class VoResponseHistoryListLogData implements Serializable {

    private String id = "";
    private String scanning_start_time = "";
    private String is_completed = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScanning_start_time() {
        return scanning_start_time;
    }

    public void setScanning_start_time(String scanning_start_time) {
        this.scanning_start_time = scanning_start_time;
    }

    public String getIs_completed() {
        return is_completed;
    }

    public void setIs_completed(String is_completed) {
        this.is_completed = is_completed;
    }
}
