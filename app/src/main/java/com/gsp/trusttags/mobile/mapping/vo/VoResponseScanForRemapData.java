package com.gsp.trusttags.mobile.mapping.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class VoResponseScanForRemapData implements Serializable {

    private String id = "";
    private String skuName = "";
    private ArrayList<String> subCodes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public ArrayList<String> getSubCodes() {
        return subCodes;
    }

    public void setSubCodes(ArrayList<String> subCodes) {
        this.subCodes = subCodes;
    }
}
