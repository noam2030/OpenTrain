package com.opentrain.app.model;

/**
 * Created by noam on 30/05/15.
 */
public class Settings {

    public static final long SCAN_INTERVAL_TEST = 2000;
    public static final long SCAN_INTERVAL_TRAIN = 15000;
    public static final String STATION_SSID_MOCK = "WiredSSID";
    public static final String STATION_SSID_TRAIN = "S-ISRAEL-RAILWAYS";
    public static final String url_get_map_from_server = "http://otrain.org:8000/data/ls";
    public static final String url_add_map_to_server = "http://otrain.org:8000/data/submit";

    public static long SCAN_INTERVAL = SCAN_INTERVAL_TRAIN;
    public static String stationSSID = STATION_SSID_TRAIN;


    public static void setTestSettings() {
        SCAN_INTERVAL = SCAN_INTERVAL_TEST;
        stationSSID = STATION_SSID_MOCK;
    }

    public static void setDefaultettings() {
        SCAN_INTERVAL = SCAN_INTERVAL_TRAIN;
        stationSSID = STATION_SSID_TRAIN;
    }

}
