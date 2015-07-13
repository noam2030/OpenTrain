package com.opentrain.app;

import android.content.Intent;

import com.opentrain.app.service.ScannerService;
import com.opentrain.app.view.MainActivity;

/**
 * Created by noam on 13/07/15.
 */
public class MockMainActivity extends MainActivity {

    protected Intent getServiceIntent() {
        return new Intent(this, MockScannerService.class);
    }
}
