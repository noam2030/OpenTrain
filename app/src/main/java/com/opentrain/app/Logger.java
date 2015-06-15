package com.opentrain.app;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by noam on 29/05/15.
 */
public class Logger {

    private static final String TAG = "Logger";

    public static void log(final String str) {
        Log.d(TAG, str);
        logItems.add(new LogItem(str));
    }

    private static final ArrayList<LogItem> logItems = new ArrayList<>();

    public static ArrayList<LogItem> getLogItems() {
        return (ArrayList<LogItem>) logItems.clone();
    }

    public static void clearItems() {
        logItems.clear();
    }
}
