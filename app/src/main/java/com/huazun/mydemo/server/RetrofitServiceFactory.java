package com.huazun.mydemo.server;

import com.google.gson.GsonBuilder;
import com.huazun.mydemo.ServerEnvironment;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceFactory {

    private static final int CONNECTION_TIMEOUT = 60;

    private static OkHttpClient s_httpClient = null;
    private static Retrofit s_retrofit = null;

    public static Retrofit retrofit() {
        if (s_retrofit == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.addInterceptor(new OkHttpInterceptor());

            s_httpClient = httpClientBuilder
                    .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .build();

            s_retrofit = new Retrofit.Builder()
                    .baseUrl(ServerEnvironment.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                    .client(s_httpClient)
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
