package com.example.android.popularmovie;

import com.example.android.popularmovie.data.Movie;
import com.example.android.popularmovie.data.Review;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbService {
    @GET("3/movie/popular")
    Call<Movie[]> getPopularMovies(@Query("api_key") String apiKey);

    @GET("3/movie/top_rated")
    Call<Movie[]> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("3/movie/{id}/videos")
    Call<Movie[]> getTrailers(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/reviews")
    Call<Review[]> getReviews(@Path("id") int id, @Query("api_key") String apiKey);
}