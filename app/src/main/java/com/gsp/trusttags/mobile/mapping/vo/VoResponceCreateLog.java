package com.gsp.trusttags.mobile.mapping.vo;

import java.io.Serializable;

public class VoResponceCreateLog implements Serializable {

    String success = "";
    String message = "";
    String status_code = "";
    String shipper_size = "";

    VoResponceCreateLogData data;

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

    public VoResponceCreateLogData getData() {
        return data;
    }

    public void setData(VoResponceCreateLogData data) {
        this.data = data;
    }

    public String getShipper_size() {
        return shipper_size;
    }

    public void setShipper_size(String shipper_size) {
        this.shipper_size = shipper_size;
    }

    public class VoResponceCreateLogData implements Serializable {

        String id = "";
        String company_id = "";
        String employee_id = "";
        String product_id = "";
        String scanning_start_time = "";
        String is_completed = "";
        String updatedAt = "";
        String createdAt = "";
        String scanning_end_time = "";

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

        public String getIs_completed() {
            return is_completed;
        }

        public void setIs_completed(String is_completed) {
            this.is_completed = is_completed;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getScanning_end_time() {
            return scanning_end_time;
        }

        public void setScanning_end_time(String scanning_end_time) {
            this.scanning_end_time = scanning_end_time;
        }
    }
}
