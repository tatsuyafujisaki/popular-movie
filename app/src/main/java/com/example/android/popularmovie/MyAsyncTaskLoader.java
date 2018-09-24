package com.example.android.popularmovie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.popularmovie.Utils.Network;

import java.io.IOException;

class MyAsyncTaskLoader extends AsyncTaskLoader<String> {
    private final String queryUrl;
    private String cachedResponse;

    MyAsyncTaskLoader(@NonNull Context context, String queryUrl) {
        super(context);
        this.queryUrl = queryUrl;
    }

    @Override
    protected void onStartLoading() {
        if (cachedResponse == null) {
            forceLoad();
        } else {
            // Falls here when a user presses HOME button during loadInBackground, and comes back after the data is downloaded.
            deliverResult(cachedResponse);
        }
    }

    @Override
    public String loadInBackground() {
        try {
            return Network.getResponse(queryUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deliverResult(String data) {
        cachedResponse = data;
        super.deliverResult(data);
    }
}