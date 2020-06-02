package com.huazun.mydemo.server;

import com.google.gson.Gson;
import com.huazun.mydemo.ServerEnvironment;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUploadFileServiceFactory {
    private static final int CONNECTION_TIMEOUT = 60;

    private static OkHttpClient s_httpClient = null;
    private static Retrofit s_retrofit = null;

    public static Retrofit retrofit() {
        if (s_retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);

            s_httpClient = builder.build();

            s_retrofit = new Retrofit.Builder()
                    .baseUrl(ServerEnvironment.BASE_URL)
                    .client(s_httpClient)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .build();
        }

        return s_retrofit;
    }

    public static String baseUrl() {
        return retrofit().baseUrl().toString();
    }

    public static void resetRetrofit() {
        s_retrofit = null;
    }
}
