package com.huazun.mydemo.server.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseCode {
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

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
