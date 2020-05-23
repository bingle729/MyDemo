package com.huazun.mydemo.server.user;

import com.huazun.mydemo.server.RetrofitServiceFactory;
import com.huazun.mydemo.utilities.AppLog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class UserAPI {
    public static final String USER_LOGIN = "authentication/userLogin";
    public static final String RESET_PASSWORD = "authentication/resetPassword";
    public static final String FORGOT_PASSWORD = "authentication/forgotPassword";
    public static final String CHANGE_PASSWORD = "authentication/changePassword";

    private interface UserService{
        @POST(USER_LOGIN)
        Call<UserLoginResponse> userLogin(@Body UserLoginRequest requestBody);

        @POST(RESET_PASSWORD)
        Call<UserLoginResponse> resetPassword(@Body UserLoginRequest requestBody);

        @POST(FORGOT_PASSWORD)
        Call<UserLoginResponse> forgotPassword(@Body UserForgotPasswordRequest requestBody);

        @POST(CHANGE_PASSWORD)
        Call<UserLoginResponse> changePassword(@Body UserChangePasswordRequest requestBody);
    }

    private static UserService userService() {
        return RetrofitServiceFactory.retrofit().create(UserService.class);
    }

    public static void userLogin(String userName, String password, final Callback<UserLoginResponse> callback) {
        UserLoginRequest request = new UserLoginRequest(userName, password);

        userService().userLogin(request).enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                if (callback != null) {
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                AppLog.log("UserService.userLogin() failed.  Reason==" + t.getLocalizedMessage());
                if (callback != null) {
                    callback.onFailure(call, t);
                }
            }
        });
    }

    public static void resetPassword(String userName, String password, final Callback<UserLoginResponse> callback) {
        UserLoginRequest request = new UserLoginRequest(userName, password);

        userService().resetPassword(request).enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                if (callback != null) {
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                AppLog.log("UserService.resetPassword() failed.  Reason==" + t.getLocalizedMessage());
                if (callback != null) {
                    callback.onFailure(call, t);
                }
            }
        });
    }

    public static void forgotPassword(String userName, final Callback<UserLoginResponse> callback) {
        UserForgotPasswordRequest request = new UserForgotPasswordRequest(userName);

        userService().forgotPassword(request).enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                if (callback != null) {
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                AppLog.log("UserService.forgotPassword() failed.  Reason==" + t.getLocalizedMessage());
                if (callback != null) {
                    callback.onFailure(call, t);
                }
            }
        });
    }

    public static void changePassword(String userName, String password, String confirmationCode, final Callback<UserLoginResponse> callback) {
        UserChangePasswordRequest request = new UserChangePasswordRequest(userName, password, confirmationCode);

        userService().changePassword(request).enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                if (callback != null) {
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                AppLog.log("UserService.changePassword() failed.  Reason==" + t.getLocalizedMessage());
                if (callback != null) {
                    callback.onFailure(call, t);
                }
            }
        });
    }
}
