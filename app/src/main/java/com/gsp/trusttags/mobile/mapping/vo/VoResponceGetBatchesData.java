package com.gsp.trusttags.mobile.mapping.vo;

import java.io.Serializable;

public class VoResponceGetBatchesData implements Serializable {

    String id = "";
    String random_id = "";
    String name = "";
    String manufacturing_date = "";
    String expiry_date = "";
    String batch_no = "";
    String product_mrp = "";
    String brand_id = "";
    String product_id = "";
    String varient_id = "";
    String production_site_id = "";
    String production_line_id = "";
    String batch_size = "";
    String printed_code = "";
    boolean status = false;
    boolean is_deleted = false;
    String created_by = "";
    String updated_by = "";
    String batch_status = "";
    String createdAt = "";
    String updatedAt = "";

    public String getScanSequence() {
        return scanSequence;
    }

    public void setScanSequence(String scanSequence) {
        this.scanSequence = scanSequence;
    }

    String scanSequence = "";


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getProduct_mrp() {
        return product_mrp;
    }

    public void setProduct_mrp(String product_mrp) {
        this.product_mrp = product_mrp;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getVarient_id() {
        return varient_id;
    }

    public void setVarient_id(String varient_id) {
        this.varient_id = varient_id;
    }

    public String getProduction_site_id() {
        return production_site_id;
    }

    public void setProduction_site_id(String production_site_id) {
        this.production_site_id = production_site_id;
    }

    public String getProduction_line_id() {
        return production_line_id;
    }

    public void setProduction_line_id(String production_line_id) {
        this.production_line_id = production_line_id;
    }

    public String getBatch_size() {
        return batch_size;
    }

    public void setBatch_size(String batch_size) {
        this.batch_size = batch_size;
    }

    public String getPrinted_code() {
        return printed_code;
    }

    public void setPrinted_code(String printed_code) {
        this.printed_code = printed_code;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
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

    public String getBatch_status() {
        return batch_status;
    }

    public void setBatch_status(String batch_status) {
        this.batch_status = batch_status;
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

    @Override
    public String toString() {
        return batch_no;
    }
}
