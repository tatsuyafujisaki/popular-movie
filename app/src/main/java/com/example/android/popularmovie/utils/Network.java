package com.example.android.popularmovie.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.android.popularmovie.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class Network {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public static RequestCreator picasso(String path){
        return Picasso.get()
                      .load(path)
                      .placeholder(R.drawable.ic_placeholder)
                      .error(R.drawable.ic_error);
    }
}