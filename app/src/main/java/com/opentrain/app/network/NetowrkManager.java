package com.opentrain.app.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.opentrain.app.Logger;
import com.opentrain.app.Settings;
import com.opentrain.app.model.MainModel;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by noam on 29/06/15.
 */
public class NetowrkManager {

    private static RequestQueue requestQueue;
    private static NetowrkManager mInstance;

    public interface RequestListener {
        void onResponse(Object response);

        void onError();
    }

    public void init(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    private NetowrkManager() {

    }

    public static NetowrkManager getInstance() {
        if (mInstance == null) {
            mInstance = new NetowrkManager();
        }
        return mInstance;
    }

    public void getMapFromSErver(final RequestListener requestListener) {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Settings.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> mapFromString = getMapFromString(response);
                            MainModel.getInstance().updateMap(mapFromString);
                            requestListener.onResponse(mapFromString);
                            StringBuilder sb = new StringBuilder();
                            for (Map.Entry<String, String> entry : mapFromString.entrySet()) {
                                sb.append(entry.getKey());
                                sb.append("/");
                                sb.append(entry.getValue());
                                sb.append("\n");
                            }
                            Logger.log(sb.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Logger.log(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestListener.onError();
                Logger.log("Error while getting map from server");
            }
        });
        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }

    private HashMap<String, String> getMapFromString(String response) throws Exception {

        InputStream stream = new ByteArrayInputStream(response.getBytes("UTF-8"));

        BufferedReader reader = new BufferedReader(new
                InputStreamReader(stream, "UTF-8"));

        HashMap<String, String> map = new HashMap<>();
        String line;
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
            Logger.log(e.toString());
        }
        return map;
    }
}
