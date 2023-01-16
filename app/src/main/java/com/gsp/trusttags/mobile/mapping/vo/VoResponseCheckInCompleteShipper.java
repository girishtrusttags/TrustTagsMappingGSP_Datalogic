package com.gsp.trusttags.mobile.mapping.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class VoResponseCheckInCompleteShipper implements Serializable {


    String success = "";
    String shipper_size = "";
    String batchId = "";
    String batchNo = "";

    boolean isShipperAllocated = false;
    String shipperId = "";

    VoResponceLog log;

    public VoResponceLogs getLogs() {
        return logs;
    }

    public void setLogs(VoResponceLogs logs) {
        this.logs = logs;
    }

    VoResponceLogs logs;

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    boolean isCompleted =  true;

    ArrayList<VoResponseIncompleteShipperList> incomplete_shipperlist = new ArrayList<>();

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getShipper_size() {
        return shipper_size;
    }

    public void setShipper_size(String shipper_size) {
        this.shipper_size = shipper_size;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public VoResponceLog getLog() {
        return log;
    }

    public void setLog(VoResponceLog log) {
        this.log = log;
    }

    public ArrayList<VoResponseIncompleteShipperList> getIncomplete_shipperlist() {
        return incomplete_shipperlist;
    }

    public void setIncomplete_shipperlist(ArrayList<VoResponseIncompleteShipperList> incomplete_shipperlist) {
        this.incomplete_shipperlist = incomplete_shipperlist;
    }

    public boolean isShipperAllocated() {
        return isShipperAllocated;
    }

    public void setShipperAllocated(boolean shipperAllocated) {
        isShipperAllocated = shipperAllocated;
    }

    public String getShipperId() {
        return shipperId;
    }

    public void setShipperId(String shipperId) {
        this.shipperId = shipperId;
    }

    public class VoResponceLog implements Serializable {

        String id = "";
        String company_id = "";
        String employee_id = "";
        String product_id = "";
        String scanning_start_time = "";
        String scanning_end_time = "";
        boolean is_completed = false;
        String createdAt = "";
        String updatedAt = "";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public String getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(String employee_id) {
            this.employee_id = employee_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getScanning_start_time() {
            return scanning_start_time;
        }

        public void setScanning_start_time(String scanning_start_time) {
            this.scanning_start_time = scanning_start_time;
        }

        public String getScanning_end_time() {
            return scanning_end_time;
        }

        public void setScanning_end_time(String scanning_end_time) {
            this.scanning_end_time = scanning_end_time;
        }

        public boolean isIs_completed() {
            return is_completed;
        }

        public void setIs_completed(boolean is_completed) {
            this.is_completed = is_completed;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }


    public VoResponceGetBatchesScanSequence getScanSequence() {
        return scanSequence;
    }

    public void setScanSequence(VoResponceGetBatchesScanSequence scanSequence) {
        this.scanSequence = scanSequence;
    }

    private VoResponceGetBatchesScanSequence scanSequence = new VoResponceGetBatchesScanSequence();

    public class VoResponceLogs implements Serializable {
        ArrayList<VoScanResponceLogs> skuLogs = new ArrayList<>();
        ArrayList<VoScanResponceLogs> fstInnerLogs = new ArrayList<>();
        ArrayList<VoScanResponceLogs> sndInnerLogs = new ArrayList<>();

        public ArrayList<VoScanResponceLogs> getSkuLogs() {
            return skuLogs;
        }

        public void setSkuLogs(ArrayList<VoScanResponceLogs> skuLogs) {
            this.skuLogs = skuLogs;
        }

        public ArrayList<VoScanResponceLogs> getFstInnerLogs() {
            return fstInnerLogs;
        }

        public void setFstInnerLogs(ArrayList<VoScanResponceLogs> fstInnerLogs) {
            this.fstInnerLogs = fstInnerLogs;
        }

        public ArrayList<VoScanResponceLogs> getSndInnerLogs() {
            return sndInnerLogs;
        }

        public void setSndInnerLogs(ArrayList<VoScanResponceLogs> sndInnerLogs) {
            this.sndInnerLogs = sndInnerLogs;
        }

        public VoResponceLastScan getLastScanned() {
            return lastScanned;
        }

        public void setLastScanned(VoResponceLastScan lastScanned) {
            this.lastScanned = lastScanned;
        }

        VoResponceLastScan lastScanned;
/*        VoScanResponceLogs skuLogs;
        VoScanResponceLogs fstInnerLogs;
        VoScanResponceLogs sndInnerLogs;
        VoResponceLastScan lastScanned;*/
        String productId="";
        String batchId="";
        String shipperSize="";
        String firstInnerSize="";

        public String getBatchNo() {
            return batchNo;
        }

        public void setBatchNo(String batchNo) {
            this.batchNo = batchNo;
        }

        String batchNo="";
        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public String getShipperSize() {
            return shipperSize;
        }

        public void setShipperSize(String shipperSize) {
            this.shipperSize = shipperSize;
        }

        public String getFirstInnerSize() {
            return firstInnerSize;
        }

        public void setFirstInnerSize(String firstInnerSize) {
            this.firstInnerSize = firstInnerSize;
        }

        public String getSecondInnerSize() {
            return secondInnerSize;
        }

        public void setSecondInnerSize(String secondInnerSize) {
            this.secondInnerSize = secondInnerSize;
        }

        String secondInnerSize="";


    }

    public class VoScanResponceLogs implements Serializable {

        String id = "";
        boolean isCompleted = true;
        ArrayList<VoResponceQrCodes> qrCodes = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public void setCompleted(boolean completed) {
            isCompleted = completed;
        }

        public ArrayList<VoResponceQrCodes> getQrCodes() {
            return qrCodes;
        }

        public void setQrCodes(ArrayList<VoResponceQrCodes> qrCodes) {
            this.qrCodes = qrCodes;
        }
    }

    public class VoResponceQrCodes implements Serializable {

        String qrCode = "";
        boolean isScan = true;
        String packageRefId = "";

        String qrId = "";
        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        public boolean isScan() {
            return isScan;
        }

        public void setScan(boolean scan) {
            isScan = scan;
        }

        public String getPackageRefId() {
            return packageRefId;
        }

        public void setPackageRefId(String packageRefId) {
            this.packageRefId = packageRefId;
        }

        public String getQrId() {
            return qrId;
        }

        public void setQrId(String qrId) {
            this.qrId = qrId;
        }


    }

    public class VoResponceLastScan implements Serializable {

        String lastLogId = "";
        String qrCode = "";
        String title = "";

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLastLogId() {
            return lastLogId;
        }

        public void setLastLogId(String lastLogId) {
            this.lastLogId = lastLogId;
        }

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }


    }
}
