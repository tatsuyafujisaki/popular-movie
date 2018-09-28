package com.example.android.popularmovie;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TmdbService {
    @GET("3/movie/popular")
    Call<ResponseBody> getPopularMovies(@Query("api_key") String apiKey);

    @GET("3/movie/top_rated")
    Call<ResponseBody> getTopRatedMovies(@Query("api_key") String apiKey);
}