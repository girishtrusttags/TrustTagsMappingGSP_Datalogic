package com.gsp.trusttags.mobile.mapping.vo;

import java.io.Serializable;

public class VoResponseFetchSKUData implements Serializable {

    String id = "";
    String sku = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name="";
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Override
    public String toString() {
        return sku +" ("+name+")";
    }

}
