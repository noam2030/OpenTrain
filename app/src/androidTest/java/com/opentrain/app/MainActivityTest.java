package com.opentrain.app;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;

import com.opentrain.app.model.MainModel;
import com.opentrain.app.model.Settings;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by noam on 13/07/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MockMainActivity> {

    private static final String TAG = MainActivityTest.class.getSimpleName();
    MockMainActivity mainActivity;
    Button button;

    CountDownLatch countDownLatch = new CountDownLatch(1);

    public MainActivityTest() {
        super(MockMainActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
        button = (Button) mainActivity.findViewById(R.id.button);
        Settings.SCAN_INTERVAL = Settings.SCAN_INTERVAL_TEST;
        Settings.stationSSID = Settings.STATION_SSID_MOCK;

        HashMap<String, String> mockResult = new HashMap<>();
        mockResult.put("1", "Station 1");
        mockResult.put("2", "Station 2");
        mockResult.put("3", "Station 3");
        mockResult.put("4", "Station 4");
        MainModel.getInstance().updateMap(mockResult);

        MockWifiScanner.mockWifiScanListener = new MockWifiScanner.MockWifiScanListener() {
            @Override
            public void onScanDone() {
                try {
                    runTestOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            button.performClick();
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    countDownLatch.countDown();
                                }
                            });
                        }
                    });
                } catch (Throwable e) {

                }
            }
        };
    }

    public void testTrip() throws Throwable {

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });

        countDownLatch.await();
    }
}
