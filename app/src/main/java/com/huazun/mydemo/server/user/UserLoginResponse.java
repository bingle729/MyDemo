package com.huazun.mydemo.server.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.huazun.mydemo.model.user.User;
import com.huazun.mydemo.server.common.BaseResponse;

public class UserLoginResponse  extends BaseResponse {
    @SerializedName("user")
    @Expose
    private User userInfo;

    public User getUserInfo() {
        return userInfo;
    }
}
