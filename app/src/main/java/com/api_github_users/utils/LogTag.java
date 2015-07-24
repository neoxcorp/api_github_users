package com.api_github_users.utils;

import android.util.Log;

public class LogTag {
    private final static String LOG_TAG= "LOG_TAG";

    public static void d(String msg) {
        Log.d(LOG_TAG, msg);
    }

    public static void e(String msg) {
        Log.e(LOG_TAG, msg);
    }

    public static void v(String msg) {
        Log.v(LOG_TAG, msg);
    }

    public static void i(String msg) {
        Log.i(LOG_TAG, msg);
    }

    public static void w(String msg) {
        Log.w(LOG_TAG, msg);
    }
}