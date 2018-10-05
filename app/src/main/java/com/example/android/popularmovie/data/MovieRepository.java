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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    public enum MovieType { POPULAR, TOP_RATED }

    private final TmdbService tmdbService;
    private final MovieDatabase movieDatabase;
    private final MovieDao movieDao;
    private final Executor executor;
    private final String posterBaseUrl;
    private String errorMessage;

    private final HashMap<MovieType, LiveData<List<Movie>>> cached = new HashMap<>();
    private final HashMap<MovieType, LocalDateTime> lastCached = new HashMap<>();

    public MovieRepository(TmdbService tmdbService, MovieDatabase movieDatabase, Executor executor, String posterBaseUrl) {
        this.tmdbService = tmdbService;
        this.movieDatabase = movieDatabase;
        this.movieDao = movieDatabase.movieDao();
        this.executor = executor;
        this.posterBaseUrl = posterBaseUrl;
    }

    public ApiResponse<LiveData<List<Movie>>> getPopularMovies() {
        return getMovies(MovieType.POPULAR);
    }

    public ApiResponse<LiveData<List<Movie>>> getTopRatedMovies() {
        return getMovies(MovieType.TOP_RATED);
    }

    public ApiResponse<LiveData<List<Movie>>> getMovies(MovieType movieType) {
        if(cached.containsKey(movieType)) {
            return ApiResponse.success(cached.get(movieType));
        }

        errorMessage = null;

        if (hasExpired(movieType)) {
            executor.execute(movieDatabase::clearAllTables);

            tmdbService.getPopularMovies(BuildConfig.API_KEY).enqueue(new Callback<Movie[]>() {
                @Override
                public void onResponse(@NonNull Call<Movie[]> call, @NonNull Response<Movie[]> response) {
                    if (response.isSuccessful()) {
                        List<Movie> movies = Converter.toArrayList(response.body());

                        for (Movie movie : movies) {
                            movie.posterPath = posterBaseUrl.concat(movie.posterPath);
                        }

                        executor.execute(() -> movieDao.save(movies));
                        cached.put(movieType, movieDao.load());
                        lastCached.put(movieType, LocalDateTime.now());
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
        }

        return errorMessage == null ? ApiResponse.success(movieDao.load()) : ApiResponse.failure(errorMessage);
    }

    private boolean hasExpired(MovieType movieType) {
        int MINUTES_TO_EXPIRE = 60;
        return !lastCached.containsKey(movieType) || MINUTES_TO_EXPIRE < ChronoUnit.MINUTES.between(lastCached.get(movieType), LocalDateTime.now());
    }
}