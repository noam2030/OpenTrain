package com.opentrain.app;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Created by noam on 26/05/15.
 */
public class GetContentFromServer extends AsyncTask<Void, Void, String> {

    HashMap<String, String> map;

    public interface GetContentListener {
        void onGetContentResults(HashMap<String, String> map);
    }

    public GetContentListener getContentListener;

    public GetContentFromServer(GetContentListener listener) {
        getContentListener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {

        return checkForStationName();

    }

    @Override
    protected void onPostExecute(String s) {
        if (getContentListener != null) {
            getContentListener.onGetContentResults(map);
        }

    }

    public String checkForStationName() {
        String result = "";
        try {
            URLConnection conn = new URL(Settings.url).openConnection();

            InputStream in = conn.getInputStream();
            HashMap<String, String> tempMap = convertStreamToString(in);

            if (tempMap != null) {
                map = tempMap;
            }

        } catch (Exception e) {
            result = "error occured while trying to connect to server: " + e.toString();
        }

        return result;
    }

    private HashMap<String, String> convertStreamToString(InputStream is) throws UnsupportedEncodingException {

        BufferedReader reader = new BufferedReader(new
                InputStreamReader(is, "UTF-8"));

        HashMap<String, String> map = new HashMap<>();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                String[] strs = line.split(" ");

                if (strs.length > 0) {
                    String key = strs[0];

                    String value = "";
                    if (strs.length > 1) {
                        for (int i = 1; i < strs.length; i++) {
                            value += strs[i] + " ";
                        }
                    }
                    map.put(key, value);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
