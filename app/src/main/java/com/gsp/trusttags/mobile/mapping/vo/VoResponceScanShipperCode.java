package com.gsp.trusttags.mobile.mapping.vo;

import java.io.Serializable;

public class VoResponceScanShipperCode implements Serializable {

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
        String random_id = "";
        String grand_parent_qr_code = "";
        String product_id = "";
        String sku = "";
        String batch_id = "";
        String total_cases = "";
        String is_scaned = "";
        String is_deleted = "";
        String status = "";
        String created_by = "";
        String updated_by = "";
        String unique_code = "";
        String company_id = "";
        String createdAt = "";
        String updatedAt = "";

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

        public String getGrand_parent_qr_code() {
            return grand_parent_qr_code;
        }

        public void setGrand_parent_qr_code(String grand_parent_qr_code) {
            this.grand_parent_qr_code = grand_parent_qr_code;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getBatch_id() {
            return batch_id;
        }

        public void setBatch_id(String batch_id) {
            this.batch_id = batch_id;
        }

        public String getTotal_cases() {
            return total_cases;
        }

        public void setTotal_cases(String total_cases) {
            this.total_cases = total_cases;
        }

        public String getIs_scaned() {
            return is_scaned;
        }

        public void setIs_scaned(String is_scaned) {
            this.is_scaned = is_scaned;
        }

        public String getIs_deleted() {
            return is_deleted;
        }

        public void setIs_deleted(String is_deleted) {
            this.is_deleted = is_deleted;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreated_by() {
            return created_by;
        }

        public void setCreated_by(String created_by) {
            this.created_by = created_by;
        }

        public String getUpdated_by() {
            return updated_by;
        }

        public void setUpdated_by(String updated_by) {
            this.updated_by = updated_by;
        }

        public String getUnique_code() {
            return unique_code;
        }

        public void setUnique_code(String unique_code) {
            this.unique_code = unique_code;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
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
}
