package com.opentrain.app;

import android.content.Context;

import com.opentrain.app.model.ScanResultItem;
import com.opentrain.app.model.Settings;
import com.opentrain.app.service.WifiScanner;

import java.util.ArrayList;

/**
 * Created by noam on 13/07/15.
 */
public class MockWifiScanner extends WifiScanner {

    public MockWifiScanner(Context context) {
        super(context);
    }

    public void startScanning() {
        reportScanResult(getScanResult());
    }

    private ArrayList<ScanResultItem> getScanResult() {
        ScanResultItem scanResultItem = new ScanResultItem();
        scanResultItem.BSSID = "1234";
        scanResultItem.SSID = Settings.stationSSID;

        ArrayList<ScanResultItem> mockList = new ArrayList<>();
        mockList.add(scanResultItem);
        return mockList;
    }
}
