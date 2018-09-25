package com.example.android.popularmovie;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;

class TmdbServiceWrapper {
    private static TmdbService instance;
    private static Callback<ResponseBody> callback;

    private TmdbServiceWrapper() {
    }

    static void init(String baseUrl, Callback<ResponseBody> callback_) {
        if (instance == null) {
            instance = new Retrofit.Builder().baseUrl(baseUrl).build().create(TmdbService.class);
            callback = callback_;
        }
    }

    static void DownloadPopularMovies() {
        instance.getPopularMovies(BuildConfig.API_KEY).enqueue(callback);
    }

    static void DownloadTopRatedMovies() {
        instance.getTopRatedMovies(BuildConfig.API_KEY).enqueue(callback);
    }
}