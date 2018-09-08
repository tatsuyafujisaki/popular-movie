package com.example.android.popularmovie;

interface AsyncTaskCallback {
    void onPreExecute();

    void onPostExecute(String result);
}