package com.example.android.popularmovie;

import com.example.android.popularmovie.data.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TmdbService {
    @GET("3/movie/popular")
    Call<Movie[]> getPopularMovies(@Query("api_key") String apiKey);

    @GET("3/movie/top_rated")
    Call<Movie[]> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("3/movie/{id}/videos")
    Call<Movie[]> getTrailers(@Query("api_key") String apiKey);
}