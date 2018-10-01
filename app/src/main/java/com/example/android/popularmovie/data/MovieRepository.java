package com.example.android.popularmovie.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.popularmovie.BuildConfig;
import com.example.android.popularmovie.TmdbService;
import com.example.android.popularmovie.utils.ApiResponse;
import com.example.android.popularmovie.utils.Converter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    public enum MovieType { POPULAR, TOP_RATED }

    private LocalDateTime lastUpdate;
    private MovieType lastUpdatedMovieType;

    private final TmdbService tmdbService;
    private final MovieDatabase movieDatabase;
    private final Executor executor;
    private final Callback<Movie[]> callback;
    private String errorMessage;

    public MovieRepository(TmdbService tmdbService, MovieDatabase movieDatabase, Executor executor, String posterBaseUrl) {
        this.tmdbService = tmdbService;
        this.movieDatabase = movieDatabase;
        this.executor = executor;

        callback = new Callback<Movie[]>() {
            @Override
            public void onResponse(@NonNull Call<Movie[]> call, @NonNull Response<Movie[]> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = Converter.toArrayList(response.body());

                    for (Movie movie : movies) {
                        movie.setPosterPath(posterBaseUrl.concat(movie.getPosterPath()));
                    }

                    executor.execute(() -> movieDatabase.movieDao().save(movies));
                } else {
                    try {
                        errorMessage = Objects.requireNonNull(response.errorBody()).string();
                    } catch (IOException e) {
                        errorMessage = e.getMessage();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie[]> call, @NonNull Throwable t) {
                errorMessage = t.getMessage();
            }
        };
    }

    public ApiResponse<LiveData<List<Movie>>> getPopularMovies() {
        return getMovies(MovieType.POPULAR);
    }

    public ApiResponse<LiveData<List<Movie>>> getTopRatedMovies() {
        return getMovies(MovieType.TOP_RATED);
    }

    private ApiResponse<LiveData<List<Movie>>> getMovies(MovieType movieType) {
        errorMessage = null;

        if (lastUpdatedMovieType != movieType || hasExpired()) {
            executor.execute(() -> movieDatabase.clearAllTables());

            switch(movieType) {
                case POPULAR:
                    tmdbService.getPopularMovies(BuildConfig.API_KEY).enqueue(callback);
                    break;
                case TOP_RATED:
                    tmdbService.getTopRatedMovies(BuildConfig.API_KEY).enqueue(callback);
                    break;
                default:
                    throw new IllegalArgumentException(movieType.toString());
            }

            lastUpdatedMovieType = movieType;
            lastUpdate = LocalDateTime.now();
        }

        return errorMessage == null ? ApiResponse.success(movieDatabase.movieDao().load()) : ApiResponse.failure(errorMessage);
    }


    private boolean hasExpired(){
        final int MINUTES_TO_EXPIRE = 1;
        return lastUpdate == null || MINUTES_TO_EXPIRE < ChronoUnit.MINUTES.between(lastUpdate, LocalDateTime.now());
    }
}