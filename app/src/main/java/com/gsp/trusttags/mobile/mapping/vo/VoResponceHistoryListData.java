package com.gsp.trusttags.mobile.mapping.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class VoResponceHistoryListData implements Serializable {

    String batch_id = "";
    String batch_no = "";
    String manufacturing_date = "";
    String expiry_date = "";
    String createdAt = "";
    String total_shipper_count = "";
    VoResponseHistoryListLogData logDetails;
    String unique_code = "";

    public String getUnique_code() {
        return unique_code;
    }

    public void setUnique_code(String unique_code) {
        this.unique_code = unique_code;
    }

    public String getScanning_datetime() {
        return scanning_datetime;
    }

    public void setScanning_datetime(String scanning_datetime) {
        this.scanning_datetime = scanning_datetime;
    }

    String scanning_datetime="";
    ArrayList<VoResponceHistoryShipperListData> shipperlist = new ArrayList<>();

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getManufacturing_date() {
        return manufacturing_date;
    }

    public void setManufacturing_date(String manufacturing_date) {
        this.manufacturing_date = manufacturing_date;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getTotal_shipper_count() {
        return total_shipper_count;
    }

    public void setTotal_shipper_count(String total_shipper_count) {
        this.total_shipper_count = total_shipper_count;
    }

    public ArrayList<VoResponceHistoryShipperListData> getShipperlist() {
        return shipperlist;
    }

    public void setShipperlist(ArrayList<VoResponceHistoryShipperListData> shipperlist) {
        this.shipperlist = shipperlist;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public VoResponseHistoryListLogData getLogDetails() {
        return logDetails;
    }

    public void setLogDetails(VoResponseHistoryListLogData logDetails) {
        this.logDetails = logDetails;
    }
}
