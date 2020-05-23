package com.huazun.mydemo.server.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserForgotPasswordRequest {
    @SerializedName("userName")
    @Expose
    private String userName;

    public UserForgotPasswordRequest(String userName) {
        super();
        this.userName = userName;

    }
}
