package com.opentrain.app;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.opentrain.app.model.MainModel;
import com.opentrain.app.model.Settings;
import com.opentrain.app.model.Station;
import com.opentrain.app.network.NetowrkManager;

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
    }

    public void test1GetMapFromServer() throws Throwable {

        NetowrkManager.getInstance().getMapFromServer(new NetowrkManager.RequestListener() {
            @Override
            public void onResponse(Object response) {

                assertNotNull(response);
                countDownLatch.countDown();
            }

            @Override
            public void onError() {
                //fail();
                countDownLatch.countDown();
            }
        });

        HashMap<String, String> mockResult = new HashMap<>();
        mockResult.put("1", "Station 1");
        mockResult.put("2", "Station 2");
        mockResult.put("3", "Station 3");
        mockResult.put("4", "Station 4");
        MainModel.getInstance().updateMap(mockResult);

        countDownLatch.await();
    }

    public void test2AddMappingToServer() throws Throwable {

        Station station = new Station();
        station.stationName = "StationNameTest";
        station.bssids.put("b4:c7:99:0b:aa:c1", null);
        station.bssids.put("b4:c7:99:0b:d4:90", null);

        NetowrkManager.getInstance().addMappingToServer(station.getPostParam(), new NetowrkManager.RequestListener() {
            @Override
            public void onResponse(Object response) {

                assertNotNull(response);
                countDownLatch.countDown();
            }

            @Override
            public void onError() {
                //fail();
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
    }

    public void test3Functional() throws Throwable {

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });

        countDownLatch.await();
    }
}
