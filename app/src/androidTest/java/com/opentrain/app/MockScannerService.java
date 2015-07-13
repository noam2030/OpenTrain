package com.opentrain.app;

import com.opentrain.app.service.ScannerService;
import com.opentrain.app.service.WifiScanner;

/**
 * Created by noam on 13/07/15.
 */
public class MockScannerService extends ScannerService {

    public WifiScanner getWifiScanner() {
        return new MockWifiScanner(this);
    }


}
