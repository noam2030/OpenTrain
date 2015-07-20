package com.opentrain.app.utils;

import android.util.Log;

import com.opentrain.app.model.LogItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public static void logMap(HashMap<String, String> mapFromString) {
        if (mapFromString != null) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : mapFromString.entrySet()) {
                sb.append(entry.getKey());
                sb.append("/");
                sb.append(entry.getValue());
                sb.append("\n");
            }
            Logger.log(sb.toString());
        } else {
            Logger.log("map is null");
        }
    }
}
