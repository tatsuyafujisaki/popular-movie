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
    private LocalDateTime lastUpdate;

    private final TmdbService tmdbService;
    private final MovieDao movieDao;
    private final Executor executor;
    private final String posterBaseUrl;
    private String errorMessage;

    public MovieRepository(TmdbService tmdbService, MovieDao movieDao, Executor executor, String posterBaseUrl) {
        this.tmdbService = tmdbService;
        this.movieDao = movieDao;
        this.executor = executor;
        this.posterBaseUrl = posterBaseUrl;
    }

    public ApiResponse<LiveData<List<Movie>>> getPopularMovies() {
        if (hasExpired()) {
            errorMessage = null;

            tmdbService.getPopularMovies(BuildConfig.API_KEY).enqueue(new Callback<Movie[]>() {
                @Override
                public void onResponse(@NonNull Call<Movie[]> call, @NonNull Response<Movie[]> response) {
                    if (response.isSuccessful()) {
                        List<Movie> movies = Converter.toArrayList(response.body());

                        for (Movie movie : movies) {
                            movie.setPosterPath(posterBaseUrl.concat(movie.getPosterPath()));
                        }

                        executor.execute(() -> movieDao.save(movies));
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
            });

            lastUpdate = LocalDateTime.now();
        }

        return errorMessage == null ? ApiResponse.success(movieDao.load()) : ApiResponse.failure(errorMessage);
    }

    private boolean hasExpired(){
        final int MINUTES_TO_EXPIRE = 1;

        return lastUpdate == null || MINUTES_TO_EXPIRE < ChronoUnit.MINUTES.between(lastUpdate, LocalDateTime.now());
    }
}