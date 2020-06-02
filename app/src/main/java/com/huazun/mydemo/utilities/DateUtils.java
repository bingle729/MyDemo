package com.huazun.mydemo.utilities;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class DateUtils {
    public static String getDateString(long mills){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy'T'HH:mm:ss a");
        String strDate= sdf.format(mills);
        return strDate;
    }

    public static String getDataSize(long size) {
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "GB";
        } else {
            return "size: error";
        }
    }


}
