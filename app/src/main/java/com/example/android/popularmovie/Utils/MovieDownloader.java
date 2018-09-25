package com.example.android.popularmovie.Utils;

import com.example.android.popularmovie.BuildConfig;
import com.example.android.popularmovie.PreExecuteCallback;
import com.example.android.popularmovie.TheMovieDatabaseAPI;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class MovieDownloader {
    private static TheMovieDatabaseAPI instance;
    private static PreExecuteCallback preExecuteCallback;
    private static Callback<ResponseBody> postExecuteCallback;

    private MovieDownloader() {
    }

    public static void init(String baseUrl, PreExecuteCallback preExecuteCallback_, Callback<ResponseBody> postExecuteCallback_) {
        if (instance == null) {
            instance = new Retrofit.Builder().baseUrl(baseUrl).build().create(TheMovieDatabaseAPI.class);
            preExecuteCallback = preExecuteCallback_;
            postExecuteCallback = postExecuteCallback_;
        }
    }

    public static void DownloadPopularMovies() {
        preExecuteCallback.onPreExecute();
        instance.getPopularMovies(BuildConfig.API_KEY).enqueue(postExecuteCallback);
    }

    public static void DownloadTopRatedMovies() {
        preExecuteCallback.onPreExecute();
        instance.getTopRatedMovies(BuildConfig.API_KEY).enqueue(postExecuteCallback);
    }
}