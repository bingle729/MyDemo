package com.huazun.mydemo.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.huazun.mydemo.BuildConfig;
import com.huazun.mydemo.utilities.AppLog;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class OkHttpInterceptor implements Interceptor {
    private static final String HEADER_USID = "X-POS-USID";
    private static final String HEADER_DEVICE_NAME = "DeviceName";
    private static final String SECURITY_TOKEN = "securityToken";
    private static final String LAST_CGID = "lastCGID";
    private static final int MAX_RETRY_COUNT = 3;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

//        String action = getAction(request);
//
//        if (!talech.POS().data().isSecurityTokenValid() &&
//                !action.equals(SIGNIN_MERCHANT) &&
//                !action.equals(GET_SECURITY_TOKEN)) {
//
//            AuthenticationAPI.getSecurityToken();
//        }

        request = addHeader(request);


        logRequest(request);

        Response response = chain.proceed(request);

        logResponse(response);

        return response;
    }

    private String getAction(Request request) {
        return request.url().toString().substring(RetrofitServiceFactory.baseUrl().length());
    }

    private Request addHeader(Request request) {
        String action = getAction(request);

        Request.Builder requestBuilder = request.newBuilder();

//        requestBuilder.header("DeviceId", talech.getDeviceId());
//        requestBuilder.header("RequestTimeStamp", DateUtils.getServerDateString());
//        requestBuilder.header("TimeZone", TimeZone.getDefault().getID());
//        requestBuilder.header("clientVersion", talech.getVersionName());

        String deviceName = "";//talech.POS().data().getSettings().getDeviceName();

//        if (action.equals(GET_SECURITY_TOKEN) && deviceName != null && deviceName.length() > 0) {
//            requestBuilder.header(HEADER_DEVICE_NAME, deviceName);
//        }
//
//        if (!action.equals(SIGNIN_MERCHANT) &&
//                !action.equals(GET_SECURITY_TOKEN) &&
//                !action.equals(GET_MERCHANT_STORE_INFO) &&
//                !action.equals(GET_ALL_EMPLOYEES) &&
//                !action.equals(RECORD_USER_SESSION) &&
//                !action.equals(TOS_ACCEPTED) &&
//                !action.equals(GET_EMPLOYEE_UPDATES) &&
//                !action.equals(GET_LOYALTY_PROGRAMS) &&
//                !action.equals(GET_MENU_UPDATES) &&
//                !action.equals(GET_SIGNED_URL)) {
//            requestBuilder.header(HEADER_USID, talech.POS().data().getCurrentUSID() + "");
//        }
//
//        if ((action.equals(GET_SECURITY_TOKEN) ||
//                action.equals(RECORD_USER_SESSION) ||
//                action.equals(SIGN_IN_BY_PIN)) &&
//                talech.POS().data().getLastCGID() > 0) {
//            requestBuilder.header(LAST_CGID, talech.POS().data().getLastCGID() + "");
//        }
//
//        if (talech.POS().data().getAdminToken() != null &&
//                talech.POS().data().getAdminToken().length() > 0 &&
//                (action.equals(GET_SECURITY_TOKEN) ||
//                        action.equals(MERCHANT_SET_PIN) ||
//                        action.equals(TOS_ACCEPTED) ||
//                        (action.equals(CHANGE_PASSWORD) &&
//                                talech.POS().data().getSecurityToken() != null &&
//                                talech.POS().data().getSecurityToken().length() == 0))) {
//            requestBuilder.header(SECURITY_TOKEN, talech.POS().data().getAdminToken());
//        } else if (talech.POS().data().getSecurityToken() != null &&
//                talech.POS().data().getSecurityToken().length() > 0) {
//            requestBuilder.header(SECURITY_TOKEN, talech.POS().data().getSecurityToken());
//        }

        return requestBuilder.build();
    }

    private void logRequest(Request request) {
        request = request.newBuilder().build();

        String headerString = "";
        String bodyString = "";

        if (BuildConfig.DEBUG || AppLog.getDebugLevel() >= AppLog.DEBUG_LEVEL_VERBOSE) {
            headerString = "Header:\n" + getRequestHeaderValues(request) + "\n";
            bodyString = "Body:\n" + getRequestBodyString(request) + "";
        }

        AppLog.log("Request\nBase: " + RetrofitServiceFactory.baseUrl().toString() + "\nAction: " + getAction(request) + "\n" + headerString + bodyString);
    }

    private String getRequestHeaderValues(Request request) {
        StringBuilder headerStringBuilder = new StringBuilder();
        headerStringBuilder.append("{\n");

        Headers headers = request.headers();
        for (String name : headers.names()) {
            headerStringBuilder.append("  " + name + " : " + headers.get(name) + "\n");
        }

        headerStringBuilder.append("}");

        return headerStringBuilder.toString();
    }

    private String getRequestBodyString(Request request) {
        if(request.body() == null) {
            return "";
        }

        try {
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            String body = buffer.readUtf8();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(body);

            return gson.toJson(element);

        } catch (IOException e) {
            AppLog.log(e);
        }

        return "";
    }

    private void logResponse(Response response) {
        response = response.newBuilder().build();

        String bodyString = "";

        if (BuildConfig.DEBUG || AppLog.getDebugLevel() >= AppLog.DEBUG_LEVEL_VERBOSE) {
            bodyString = "Body:\n" + getResponseBodyString(response) + "";
        }

        String action = response.request().url().toString().substring(RetrofitServiceFactory.baseUrl().toString().length());

        AppLog.log("Response\nBase: " + RetrofitServiceFactory.baseUrl().toString() + "\nAction: " + action + "\n" + bodyString);
    }

    private String getResponseHeaderValues(Response response) {
        StringBuilder headerStringBuilder = new StringBuilder();
        headerStringBuilder.append("{\n");

        Headers headers = response.headers();
        for (String name : headers.names()) {
            headerStringBuilder.append("  " + name + " : " + headers.get(name) + "\n");
        }

        headerStringBuilder.append("}");

        return headerStringBuilder.toString();
    }

    private String getResponseBodyString(Response response) {
        try {
            ResponseBody responseBody = response.body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();

            String body = buffer.clone().readString(responseBody.contentType().charset(StandardCharsets.UTF_8));

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(body);

            return gson.toJson(element);
        } catch (IOException e) {
            AppLog.log(e);
        } catch (Exception e) {
            AppLog.log(e);
        }

        return "";
    }
}

