package com.example.android.popularmovie;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;

class TheMovieDatabaseClient {
    private static TheMovieDatabaseService instance;
    private static PreExecuteCallback preExecuteCallback;
    private static Callback<ResponseBody> postExecuteCallback;

    private TheMovieDatabaseClient() {
    }

    static void init(String baseUrl, PreExecuteCallback preExecuteCallback_, Callback<ResponseBody> postExecuteCallback_) {
        if (instance == null) {
            instance = new Retrofit.Builder().baseUrl(baseUrl).build().create(TheMovieDatabaseService.class);
            preExecuteCallback = preExecuteCallback_;
            postExecuteCallback = postExecuteCallback_;
        }
    }

    static void DownloadPopularMovies() {
        preExecuteCallback.onPreExecute();
        instance.getPopularMovies(BuildConfig.API_KEY).enqueue(postExecuteCallback);
    }

    static void DownloadTopRatedMovies() {
        preExecuteCallback.onPreExecute();
        instance.getTopRatedMovies(BuildConfig.API_KEY).enqueue(postExecuteCallback);
    }
}