package com.opentrain.app;

import android.content.Context;

import com.opentrain.app.model.MainModel;
import com.opentrain.app.model.ScanResultItem;
import com.opentrain.app.model.Settings;
import com.opentrain.app.service.WifiScanner;

import java.util.ArrayList;

/**
 * Created by noam on 13/07/15.
 */
public class MockWifiScanner extends WifiScanner {

    public interface MockWifiScanListener {
        void onScanDone();
    }

    ArrayList<ArrayList<ScanResultItem>> mockResultsList = new ArrayList<>();
    private int index;
    public static MockWifiScanListener mockWifiScanListener;

    public MockWifiScanner(Context context) {
        super(context);
        initMockList();
    }

    private void initMockList() {

        ArrayList<ScanResultItem> mockList;

        mockList = new ArrayList<>();
        mockList.add(getStationSannItem("1"));
        mockResultsList.add(mockList);

        mockList = new ArrayList<>();
        mockList.add(getNonStationSannItem("-1"));
        mockResultsList.add(mockList);

        mockList = new ArrayList<>();
        mockList.add(getStationSannItem("2"));
        mockResultsList.add(mockList);

        mockList = new ArrayList<>();
        mockList.add(getNonStationSannItem("-2"));
        mockResultsList.add(mockList);

        mockList = new ArrayList<>();
        mockList.add(getStationSannItem("3"));
        mockResultsList.add(mockList);

        index = 0;

        map = MainModel.getInstance().getMap();
    }

    private ScanResultItem getStationSannItem(String bssid) {
        ScanResultItem scanResultItem = new ScanResultItem();
        scanResultItem.SSID = Settings.STATION_SSID_MOCK;
        scanResultItem.BSSID = bssid;
        return scanResultItem;
    }

    private ScanResultItem getNonStationSannItem(String bssid) {
        ScanResultItem scanResultItem = new ScanResultItem();
        scanResultItem.SSID = "NoTrain";
        scanResultItem.BSSID = bssid;
        return scanResultItem;
    }

    public void startScanning() {

        if (index >= mockResultsList.size()) {
            if (mockWifiScanListener != null) {
                mockWifiScanListener.onScanDone();
            }
            return;
        }

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        reportScanResult(getScanResult());
        index++;
    }

    private ArrayList<ScanResultItem> getScanResult() {
        if (mockResultsList.size() > index) {
            return mockResultsList.get(index);
        }
        return null;
    }
}
