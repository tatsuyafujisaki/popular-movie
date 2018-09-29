package com.example.android.popularmovie.data;

import android.arch.lifecycle.LiveData;

import com.example.android.popularmovie.BuildConfig;
import com.example.android.popularmovie.TmdbService;
import com.example.android.popularmovie.utils.ApiResponse;
import com.example.android.popularmovie.utils.Converter;
import com.example.android.popularmovie.utils.GsonWrapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private final TmdbService tmdbService;
    private final MovieDao movieDao;
    private final Executor executor;
    private final String tmdbJsonResultsElement;
    private final String posterBaseUrl;
    private String errorMessage;

    MovieRepository(TmdbService tmdbService, MovieDao movieDao, Executor executor, String tmdbJsonResultsElement, String posterBaseUrl) {
        this.tmdbService = tmdbService;
        this.movieDao = movieDao;
        this.executor = executor;
        this.tmdbJsonResultsElement = tmdbJsonResultsElement;
        this.posterBaseUrl = posterBaseUrl;
    }

    ApiResponse<LiveData<List<Movie>>> getPopularMovies() {
        tmdbService.getPopularMovies(BuildConfig.API_KEY).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                List<Movie> movies;
                if (response.isSuccessful()) {
                    try {
                        movies = Converter.toArrayList(GsonWrapper.fromJson(GsonWrapper.getJsonArray(Objects.requireNonNull(response.body()).string(), tmdbJsonResultsElement), Movie[].class));

                        for (Movie movie : movies) {
                            movie.posterPath = posterBaseUrl.concat(movie.posterPath);
                        }

                        executor.execute(() -> movieDao.save(movies));
                    } catch (IOException e) {
                        errorMessage = e.getMessage();
                    }
                } else {
                    try {
                        errorMessage = Objects.requireNonNull(response.errorBody()).string();
                    } catch (IOException e) {
                        errorMessage = e.getMessage();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errorMessage = t.getMessage();
            }
        });

        return errorMessage == null ? ApiResponse.success(movieDao.load()) : ApiResponse.failure(errorMessage);
    }
}