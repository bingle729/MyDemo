package com.huazun.mydemo.server.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginRequest {
    @SerializedName("userName")
    @Expose
    private String userName;

    @SerializedName("password")
    @Expose
    private String password;

    public UserLoginRequest(String userName, String password) {
        super();

        this.userName = userName;
        this.password = password;
    }
}
