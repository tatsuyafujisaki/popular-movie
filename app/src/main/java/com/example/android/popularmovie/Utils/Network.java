package com.example.android.popularmovie.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class Network {
    private static final OkHttpClient client = new OkHttpClient();

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

        if (cm == null) {
            return false;
        }

        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnected();
    }

    public static String getResponse(String url) throws IOException {
        ResponseBody rb = client.newCall(new Request.Builder().url(url).build()).execute().body();
        return rb != null ? rb.string() : null;
    }
}