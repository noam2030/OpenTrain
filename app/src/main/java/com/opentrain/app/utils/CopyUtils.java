package com.opentrain.app.utils;

import android.content.Context;
import android.widget.Toast;

import com.opentrain.app.model.Station;

/**
 * Created by noam on 30/05/15.
 */
public class CopyUtils {

    public static void copyToClipboard(Station station, String stationName, Context context) {

        try {
            if (stationName == null || stationName.isEmpty()) {
                return;
            }

            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(station.copyUnMapped(stationName));
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("text label", station.copyUnMapped(stationName));
                clipboard.setPrimaryClip(clip);
            }
            toast(station.copyUnMapped(stationName) + "copied!", context);
        } catch (Exception e) {
            toast(e.toString(), context);
        }
    }

    private static void toast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
