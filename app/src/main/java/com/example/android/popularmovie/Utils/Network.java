package com.example.android.popularmovie.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Network {
    private static final OkHttpClient client = new OkHttpClient();

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo ni = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    public static String getResponse(String url) throws IOException {
        return client.newCall(new Request.Builder().url(url).build()).execute().body().string();
    }
}