package com.opentrain.app.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.opentrain.app.utils.Logger;
import com.opentrain.app.model.Settings;
import com.opentrain.app.model.MainModel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

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

    public void getMapFromServer(final RequestListener requestListener) {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Settings.url_get_map_from_server,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> mapFromString = getMapFromString(response);
                            MainModel.getInstance().updateMap(mapFromString);
                            requestListener.onResponse(mapFromString);
                            Logger.logMap(mapFromString);
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

    public void addMappingToServer(JSONObject jsonObject, final RequestListener requestListener) {

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Settings.url_add_map_to_server, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            requestListener.onResponse(response);
                            Logger.log(response.toString());
                        } else {
                            Logger.log("add map to server response is null");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestListener.onError();
                Logger.log("Error while adding map from to server");
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
