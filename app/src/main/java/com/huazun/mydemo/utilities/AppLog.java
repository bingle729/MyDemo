package com.huazun.mydemo.utilities;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.huazun.mydemo.DemoApplication;

import java.text.DecimalFormat;

public class AppLog {
    public static final int DEBUG_LEVEL_NONE = 0;
    public static final int DEBUG_LEVEL_INFO = 1;
    public static final int DEBUG_LEVEL_VERBOSE = 2;
    public static final int DEBUG_LEVEL_DIAGNOSE = 3;

    private static final int MAX_LOG_STACK_LINES = 6;

    private static String s_appStartDate;
    private static int s_debugLevel = DEBUG_LEVEL_VERBOSE;
    private static String s_logName = "";


    public static int getDebugLevel() {
        return s_debugLevel;
    }

    private static String getStackString(StackTraceElement element) {
        String className = element.getClassName();
        int lastDotIndex = className.lastIndexOf('.');
        if (lastDotIndex < className.length()) {
            className = className.substring(lastDotIndex + 1);
        }
        String methodName = element.getMethodName();
        int lineNumber = element.getLineNumber();

        return className + "." + methodName + "() [line " + lineNumber + "]";
    }

    public static void log(String text) {
        if(text == null || text.length() == 0) {
            return;
        }

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        StackTraceElement element = null;
        for (int i = 3; i >= 0; --i) {
            if (stackTraceElements.length > i) {
                element = stackTraceElements[i];
                break;
            }
        }

        String stackText = AppLog.getStackString(element);

        int maxLength = text.length();
        if (maxLength > 4000) {
            int fromIndex = 0;
            while (fromIndex < maxLength) {
                int toIndex = Math.min(maxLength, fromIndex + 4000);
                Log.d(stackText, text.substring(fromIndex, toIndex));
                fromIndex = toIndex;
            }
        } else {
            Log.d(stackText, text);
        }
    }

    public static void log(Exception e) {
        log(e.getLocalizedMessage());
        log(e.getStackTrace());
    }

    public static void log(StackTraceElement[] stackTraceElements) {
        StringBuilder stringBuilder = new StringBuilder();

        int logLength = (stackTraceElements.length < MAX_LOG_STACK_LINES) ? stackTraceElements.length : MAX_LOG_STACK_LINES;
        for (int i = 0; i < logLength; ++i) {
            StackTraceElement element = stackTraceElements[i];
            stringBuilder.append(getStackString(element) + "\n");
        }

        log(stringBuilder.toString());
    }

//    public static void redirectLog(String logName) {
//        s_logName = logName;
//
//        try {
//            String string = "start file\n";
//
//            FileOutputStream fos = DemoApplication.context().openFileOutput(logName, Context.MODE_PRIVATE);
//            fos.write(string.getBytes());
//            fos.close();
//
//            SharedPreferences sharedPreferences = DemoApplication.context().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString(LAST_DEBUG_LOG_NAME, logName);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public static void activateWithMerchantId(String merchantId, Long sessionId, int debugLevel) {
//
//        AppLog.log("Activate with merchantID: " + merchantId + ", sessionId: " + sessionId);
//
//        s_debugLevel = debugLevel;
//
//        //TODO: finish implementing file upload and file write
//        if (false && !BuildConfig.DEBUG) {
//            AppLog.sendLogsIfNeeded();
//
//            String logName = merchantId + "/" + sessionId + ".log";
//            AppLog.redirectLog(logName);
//        }
//
//        if (s_appStartDate == null) {
//            s_appStartDate = DateUtils.getLogDate();
//        }
//
//        //String iid = InstanceID.i(context).getTagId();
//        log("App start date: " + DateUtils.getLogDate() + ", deviceID: " + talech.getDeviceId() + ", deviceName: " + Build.PRODUCT);
//        log("DebugLevel: " + debugLevel + ", merchantID: " + merchantId + ", sessionID: " + sessionId + ", build: " + Build.BRAND + " " + Build.DEVICE + " " + Build.MODEL + " " + Build.VERSION.RELEASE + " timezone:" + TimeZone.getDefault().getDisplayName());
//        logMemory();
//        logDiskspace();
//    }

    public static void logMemory() {
        ActivityManager activityManager = (ActivityManager) DemoApplication.context().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        AppLog.log("System avail: " + bytesToString(memoryInfo.availMem) +
                " threshold: " + bytesToString(memoryInfo.threshold) +
                " lowMemory: " + memoryInfo.lowMemory);

        Runtime runtime = Runtime.getRuntime();
        AppLog.log("Runtime free: " + bytesToString(runtime.freeMemory()) +
                " total: " + bytesToString(runtime.totalMemory()) +
                " used: " + bytesToString(runtime.totalMemory() - runtime.freeMemory()) +
                " max: " + bytesToString(runtime.maxMemory()));
    }

    public static void logDiskspace() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long totalBytes = statFs.getTotalBytes();
        long freeBytes = statFs.getFreeBytes();
        long usedBytes = totalBytes - freeBytes;

        AppLog.log("Diskspace total: " + bytesToString(totalBytes) + " free: " + bytesToString(freeBytes) + " used: " + bytesToString(usedBytes));
    }

    private static String floatFormat(double d) {
        return new DecimalFormat("#.##").format(d);
    }

    private static String bytesToString(long size) {
        long KB = 1 * 1024;
        long MB = KB * 1024;
        long GB = MB * 1024;
        long TB = GB * 1024;

        if (size < KB) {
            return floatFormat(size) + " bytes";
        }
        if (size < MB) {
            return floatFormat((double) size / KB) + " KB";
        }
        if (size < GB) {
            return floatFormat((double) size / MB) + " MB";
        }
        if (size < TB) {
            return floatFormat((double) size / GB) + " GB";
        }

        return floatFormat((double) size / TB) + " TB";
    }

    private static void sendLogsIfNeeded() {

    }
}

