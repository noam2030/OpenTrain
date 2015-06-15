package com.opentrain.app;

/**
 * Created by noam on 29/05/15.
 */
public class ScanResultItem {
    public String BSSID;
    public String SSID;

    public String toString() {
        return "BSSID: " + BSSID + " ,SSID: " + SSID;
    }
}
