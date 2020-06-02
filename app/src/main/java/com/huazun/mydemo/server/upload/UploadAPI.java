package com.huazun.mydemo.server.upload;

import com.huazun.mydemo.server.RetrofitUploadFileServiceFactory;
import com.huazun.mydemo.utilities.AppLog;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class UploadAPI {
    public static final String UPLOAD_FILE = "authentication/uploadFile";
    private interface UploadFileService{
        @Multipart
        @POST(UPLOAD_FILE)
        Call<UploadFileResponse> uploadFile(@Part("description") RequestBody description, @Part MultipartBody.Part file);
    }

    private static UploadFileService uploadFileService() {
        return RetrofitUploadFileServiceFactory.retrofit().create(UploadFileService.class);
    }

    public static void uploadFile(RequestBody description, MultipartBody.Part file, final Callback<UploadFileResponse> callback) {
//        Upload request = new UserLoginRequest(userName, password);

        uploadFileService().uploadFile(description, file).enqueue(new Callback<UploadFileResponse>() {
            @Override
            public void onResponse(Call<UploadFileResponse> call, Response<UploadFileResponse> response) {
                if (callback != null) {
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<UploadFileResponse> call, Throwable t) {
                AppLog.log("UploadFileService.uploadFile() failed.  Reason==" + t.getLocalizedMessage());
                if (callback != null) {
                    callback.onFailure(call, t);
                }
            }
        });
    }
}


