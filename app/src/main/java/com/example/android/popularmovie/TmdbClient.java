package com.example.android.popularmovie;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;

class TmdbClient {
    private static TmdbService instance;
    private static Callback<ResponseBody> callback;

    private TmdbClient() {
    }

    static void init(String baseUrl, Callback<ResponseBody> callback) {
        if (instance == null) {
            instance = new Retrofit.Builder().baseUrl(baseUrl).build().create(TmdbService.class);
            TmdbClient.callback = callback;
        }
    }

    static void DownloadPopularMovies() {
        instance.getPopularMovies(BuildConfig.API_KEY).enqueue(callback);
    }

    static void DownloadTopRatedMovies() {
        instance.getTopRatedMovies(BuildConfig.API_KEY).enqueue(callback);
    }
}