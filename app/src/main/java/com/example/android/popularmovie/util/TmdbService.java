package com.example.android.popularmovie.util;

import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.room.entity.Review;
import com.example.android.popularmovie.room.entity.Trailer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbService {
    @GET("3/movie/popular")
    Call<List<Movie>> getPopularMovies(@Query("api_key") String apiKey);

    @GET("3/movie/top_rated")
    Call<List<Movie>> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("3/movie/{id}/videos")
    Call<List<Trailer>> getTrailers(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/reviews")
    Call<List<Review>> getReviews(@Path("id") int id, @Query("api_key") String apiKey);
}