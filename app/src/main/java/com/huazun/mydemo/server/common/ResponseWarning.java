package com.huazun.mydemo.server.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseWarning {
    @SerializedName("businessError")
    @Expose
    private String businessError;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

    public String getBusinessError() {
        return businessError;
    }

    public void setBusinessError(String businessError) {
        this.businessError = businessError;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}

