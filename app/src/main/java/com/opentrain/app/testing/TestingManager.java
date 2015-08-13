package com.opentrain.app.testing;

/**
 * Created by noam on 13/08/15.
 */
public class TestingManager {

    public interface TripListener {
        void onDone();
    }

    private static TestingManager mInstance;

    public static TestingManager getInstance() {
        if (mInstance == null) {
            mInstance = new TestingManager();
        }
        return mInstance;
    }

    public void testTrip(){

    }
}
