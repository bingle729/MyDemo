package com.huazun.mydemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.huazun.mydemo.utilities.AppLog;

public class DemoApplication extends Application {
    private static DemoApplication s_instance;
    private static Context s_context;

    private Activity currentActivity = null;

    public static DemoApplication POS() {
        assert (s_instance != null);
        return s_instance;
    }

    public static Context context() {
        return s_context;
    }


    public static String getExternalFilesDirectory(){
        return s_context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    @Override
    public void onCreate() {
        AppLog.log("Application Created");
        super.onCreate();

        assert (s_instance == null);
        s_instance = this;
        s_context = getApplicationContext();
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        if(this.currentActivity == null) {
            DisplayMetrics metric = new DisplayMetrics();
            currentActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);

        }

        this.currentActivity = currentActivity;
    }

}
