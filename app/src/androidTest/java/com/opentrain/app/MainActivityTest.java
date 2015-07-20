package com.opentrain.app;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.opentrain.app.network.NetowrkManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
                fail();
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
    }

    public void test2AddMappingToServer() throws Throwable {


        String stationName = "Hi Yossi!";
        ArrayList<String> routers = new ArrayList<>();
        routers.add("b4:c7:99:0b:aa:c1");
        routers.add("b4:c7:99:0b:d4:90");


        JSONArray routerArray = new JSONArray();
        for (String router : routers) {
            routerArray.put(router);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", stationName);
        jsonObject.put("bssid", routers.get(0));

        NetowrkManager.getInstance().addMappingToServer(jsonObject, new NetowrkManager.RequestListener() {
            @Override
            public void onResponse(Object response) {

                assertNotNull(response);
                countDownLatch.countDown();
            }

            @Override
            public void onError() {
                fail();
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
