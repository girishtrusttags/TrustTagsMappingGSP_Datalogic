package com.gsp.trusttags.mobile.mapping.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class VoResponceGetBatches implements Serializable {

    private String success = "";
    private String message = "";
    private String status_code = "";

    private ArrayList<VoResponceGetBatchesData> data = new ArrayList<>();

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

    public ArrayList<VoResponceGetBatchesData> getData() {
        return data;
    }

    public void setData(ArrayList<VoResponceGetBatchesData> data) {
        this.data = data;
    }

    public VoResponceGetBatchesScanSequence getScanSequence() {
        return scanSequence;
    }

    public void setScanSequence(VoResponceGetBatchesScanSequence scanSequence) {
        this.scanSequence = scanSequence;
    }

    private VoResponceGetBatchesScanSequence scanSequence = new VoResponceGetBatchesScanSequence();

}
