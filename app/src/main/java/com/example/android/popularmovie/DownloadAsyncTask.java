package com.example.android.popularmovie;

import android.os.AsyncTask;

import com.example.android.popularmovie.Utils.Network;

import java.io.IOException;

final class DownloadAsyncTask extends AsyncTask<String, Void, String> {
    private final AsyncTaskCallback asyncTaskCallback;

    DownloadAsyncTask(AsyncTaskCallback asyncTaskCallback) {
        this.asyncTaskCallback = asyncTaskCallback;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskCallback.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return Network.getResponse(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        asyncTaskCallback.onPostExecute(result);
    }
}