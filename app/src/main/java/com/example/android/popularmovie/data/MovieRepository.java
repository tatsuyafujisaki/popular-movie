package com.example.android.popularmovie.data;

import android.arch.lifecycle.LiveData;

import com.example.android.popularmovie.BuildConfig;
import com.example.android.popularmovie.TmdbService;
import com.example.android.popularmovie.utils.ApiResponse;
import com.example.android.popularmovie.utils.Converter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MovieRepository {
    private final TmdbService tmdbService;
    private final MovieDao movieDao;
    private final Executor executor;
    private final String posterBaseUrl;
    private String errorMessage;

    MovieRepository(TmdbService tmdbService, MovieDao movieDao, Executor executor, String posterBaseUrl) {
        this.tmdbService = tmdbService;
        this.movieDao = movieDao;
        this.executor = executor;
        this.posterBaseUrl = posterBaseUrl;
    }

    ApiResponse<LiveData<List<Movie>>> getPopularMovies() {
        tmdbService.getPopularMovies(BuildConfig.API_KEY).enqueue(new Callback<Movie[]>() {
            @Override
            public void onResponse(Call<Movie[]> call, Response<Movie[]> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = Converter.toArrayList(response.body());

                    for (Movie movie : movies) {
                        movie.posterPath = posterBaseUrl.concat(movie.posterPath);
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
            public void onFailure(Call<Movie[]> call, Throwable t) {
                errorMessage = t.getMessage();
            }
        });

        return errorMessage == null ? ApiResponse.success(movieDao.load()) : ApiResponse.failure(errorMessage);
    }
}