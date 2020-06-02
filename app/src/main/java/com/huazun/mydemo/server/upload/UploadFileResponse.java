package com.huazun.mydemo.server.upload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.huazun.mydemo.model.UploadFile;
import com.huazun.mydemo.server.common.BaseResponse;

public class UploadFileResponse extends BaseResponse {
    @SerializedName("uploadFile")
    @Expose
    private UploadFile uploadFile;

    public UploadFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(UploadFile uploadFile) {
        this.uploadFile = uploadFile;
    }
}
