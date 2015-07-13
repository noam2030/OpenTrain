package com.opentrain.app;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import java.util.concurrent.CountDownLatch;

/**
 * Created by noam on 13/07/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MockMainActivity> {

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

    public void testFunctional() throws Throwable {

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });

        countDownLatch.await();
    }
}
