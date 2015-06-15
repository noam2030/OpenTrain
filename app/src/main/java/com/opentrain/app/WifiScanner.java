package com.opentrain.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noam on 18/05/15.
 */
public class WifiScanner extends BroadcastReceiver {

    WifiManager mainWifi;

    boolean wasStation;
    HashMap<String, String> map = new HashMap<>();
    ArrayList<Station> stationsListItems = new ArrayList<>();
    private ScanningListener scanningListener;

    public WifiScanner(Context context) {
        mainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        // Check for wifi is disabled
        if (!mainWifi.isWifiEnabled()) {
            mainWifi.setWifiEnabled(true);
        }

        getMapFromServer();
    }

    public void startScanning() {
        mainWifi.startScan();
    }

    protected void unRegister(Context context) {
        context.unregisterReceiver(this);
    }

    protected void register(Context context) {
        context.registerReceiver(this, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        List<ScanResult> results = mainWifi.getScanResults();
        ArrayList<ScanResultItem> scanResultItems = new ArrayList<>();
        if (results != null && results.size() > 0) {
            for (ScanResult scanResult : results) {
                ScanResultItem scanResultItem = new ScanResultItem();
                scanResultItem.BSSID = scanResult.BSSID;
                scanResultItem.SSID = scanResult.SSID;
                scanResultItems.add(scanResultItem);

            }
        }
        reportScanResult(scanResultItems);
    }

    public void reportScanResult(ArrayList<ScanResultItem> results) {
        onSanningResults(results);
        if (results != null && results.size() > 0) {
            for (ScanResultItem scanResultItem : results) {
                Logger.log("scan result: " + scanResultItem.toString());
            }
        }
        if (scanningListener != null) {
            scanningListener.onSannResult();
        }
    }


    public void onSanningResults(List<ScanResultItem> scanResults) {
        if (scanResults != null && scanResults.size() > 0) {

            Station station = isStation(scanResults);
            boolean isStation = station != null;

            Logger.log("onSanningResults, isStation: " + isStation + " ,wasStation: " + wasStation);

            if (isStation) {

                setName(station);

                //enter station
                Logger.log("station name: " + station.stationName);

                boolean exist = false;
                if (stationsListItems.size() > 0) {
                    Station lastStation = stationsListItems.get(stationsListItems.size() - 1);
                    if (lastStation.isEqual(station)) {
                        //we are still in the station (probably miss one scan indication)
                        lastStation.updateBssids(station);
                        exist = true;
                    }
                }
                if (!exist) {
                    //we get in to new station
                    station.setArrive(System.currentTimeMillis());
                    stationsListItems.add(station);
                }
                Logger.log("arrive to station: " + station.stationName + ", " + "exist:" + exist);

            } else if (wasStation) {
                //exit station
                if (stationsListItems.size() > 0) {
                    Station s = stationsListItems.get(stationsListItems.size() - 1);
                    s.setDeparture(System.currentTimeMillis());
                }
                Logger.log("exit from station!");
            }

            wasStation = isStation;
        }
    }

    public Station isStation(List<ScanResultItem> scanResults) {

        Station station = null;
        for (ScanResultItem scanResult : scanResults) {

            if (Settings.stationSSID.equals(scanResult.SSID)) {

                if (station == null) {
                    station = new Station();
                }

                station.bssids.put(scanResult.BSSID, map.get(scanResult.BSSID));
            }
        }

        return station;
    }

    public void setName(Station station) {

        for (Map.Entry<String, String> entry : station.bssids.entrySet()) {
            if (entry.getValue() != null) {
                station.stationName = entry.getValue();
                return;
            }
        }

        station.stationName = "Not fount fo any BSSID";
    }

    public void onGetContentResults(HashMap<String, String> results) {
        if (results != null) {

            for (Map.Entry<String, String> serverEntry : results.entrySet()) {
                map.put(serverEntry.getKey(), serverEntry.getValue());
            }

            for (Station station : stationsListItems) {
                updateBssidMapping(station);
                setName(station);
            }
        }
    }

    private Station updateBssidMapping(Station station) {
        if (map != null) {
            for (Map.Entry<String, String> entry : station.bssids.entrySet()) {
                station.bssids.put(entry.getKey(), map.get(entry.getKey()));
            }
        }
        return station;
    }

    public void getMapFromServer() {

        new GetContentFromServer(new GetContentFromServer.GetContentListener() {
            @Override
            public void onGetContentResults(HashMap<String, String> results) {
                WifiScanner.this.onGetContentResults(results);
            }
        }).execute();
    }

    public void setScanningListener(ScanningListener scanningListener) {
        this.scanningListener = scanningListener;
    }

    public interface ScanningListener {
        void onSannResult();
    }
}
