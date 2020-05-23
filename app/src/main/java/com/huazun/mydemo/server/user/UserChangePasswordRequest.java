package com.huazun.mydemo.server.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserChangePasswordRequest {
    @SerializedName("userName")
    @Expose
    private String userName;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("confirmationCode")
    @Expose
    private String confirmationCode;

    public UserChangePasswordRequest(String userName, String password, String confirmationCode) {
        super();
        this.userName = userName;
        this.password = password;
        this.confirmationCode = confirmationCode;
    }
}
