package com.huazun.mydemo.server.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseResponse {
    public static final int RESPONSE_STATUS_CODE_OK = 200;

    public static final int RESPONSE_STATUS_CODE_USER_FIRST_LOGIN = 2012;
    public static final int RESPONSE_STATUS_CODE_USER_DUPLICATE_PASSWORD = 2014;
    public static final int RESPONSE_STATUS_CODE_USER_NOT_FOUND = 2013;

    public static final int RESPONSE_STATUS_CODE_GENERAL_SETTING_NOT_FOUND = 3012;
    public static final int RESPONSE_STATUS_CODE_CUSTOMER_NOT_FOUND = 3016;
    public static final int RESPONSE_STATUS_CODE_NO_LOYALTY_FOUND = 3024;

    public static final int RESPONSE_STATUS_CODE_INVALID_SECURITY_TOKEN = 6002;
    public static final int RESPONSE_STATUS_CODE_NO_RECORDS_FOUND = 6005;
    public static final int RESPONSE_STATUS_CODE_REQUEST_ACCEPTED = 6108;

    private static final String EXPIRED_TOKEN_DESC = "Access";

    @SerializedName("ResponseCode")
    @Expose
    private ResponseCode responseCode;
    @SerializedName("ResponseWarnings")
    @Expose
    private List<ResponseWarning> responseWarnings = null;

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public List<ResponseWarning> getResponseWarnings() {
        return responseWarnings;
    }

    public void setResponseWarnings(List<ResponseWarning> responseWarnings) {
        this.responseWarnings = responseWarnings;
    }

    public boolean isResponseOK() {
        return (getResponseCode().getStatusCode().intValue() == RESPONSE_STATUS_CODE_OK);
    }

    public boolean isFirstLogin () {
        return (getResponseCode().getStatusCode().intValue() == RESPONSE_STATUS_CODE_USER_FIRST_LOGIN);
    }

    public boolean isSecurityTokenExpired() {
        return (getResponseCode().getStatusCode().intValue() == RESPONSE_STATUS_CODE_INVALID_SECURITY_TOKEN);
    }

    public boolean isAPIError() {
        switch(getResponseCode().getStatusCode()) {
            case RESPONSE_STATUS_CODE_NO_RECORDS_FOUND:
            case RESPONSE_STATUS_CODE_NO_LOYALTY_FOUND:
            case RESPONSE_STATUS_CODE_GENERAL_SETTING_NOT_FOUND:
            case RESPONSE_STATUS_CODE_CUSTOMER_NOT_FOUND:
            case RESPONSE_STATUS_CODE_USER_NOT_FOUND:
//            case RESPONSE_STATUS_CODE_USER_FIRST_LOGIN:
                return true;
        }

        return false;
    }

    public boolean isTokenInvalid() {
        if(getResponseCode().getStatusCode() == RESPONSE_STATUS_CODE_INVALID_SECURITY_TOKEN || getResponseCode().getDesc() == EXPIRED_TOKEN_DESC) {
            return true;
        }

        return false;
    }
}
