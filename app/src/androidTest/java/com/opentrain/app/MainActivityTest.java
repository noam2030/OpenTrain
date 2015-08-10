package com.opentrain.app;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

/**
 * Created by noam on 13/07/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MockMainActivity> {

    private static final String TAG = MainActivityTest.class.getSimpleName();
    MockMainActivity mainActivity;
    Button button;

    public MainActivityTest() {
        super(MockMainActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
        button = (Button) mainActivity.findViewById(R.id.button);
    }

    public void testOnResume() throws Throwable {


    }
}
