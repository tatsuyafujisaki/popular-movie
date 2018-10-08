package com.example.android.popularmovie.room.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.popularmovie.BuildConfig;
import com.example.android.popularmovie.TmdbService;
import com.example.android.popularmovie.room.dao.MovieDao;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.utils.ApiResponse;
import com.example.android.popularmovie.utils.Converter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.popularmovie.room.repository.MovieRepository.MovieType.POPULAR;
import static com.example.android.popularmovie.room.repository.MovieRepository.MovieType.TOP_RATED;

public class MovieRepository {
    public enum MovieType { POPULAR, TOP_RATED }

    private final TmdbService tmdbService;
    private final MovieDao movieDao;
    private final Executor executor;
    private final String posterBaseUrl;
    private String errorMessage;

    private final HashMap<MovieType, LiveData<List<Movie>>> cached = new HashMap<>();
    private final HashMap<MovieType, LocalDateTime> lastCached = new HashMap<>();

    public MovieRepository(TmdbService tmdbService, MovieDao movieDao, Executor executor, String posterBaseUrl) {
        this.tmdbService = tmdbService;
        this.movieDao = movieDao;
        this.executor = executor;
        this.posterBaseUrl = posterBaseUrl;
    }

    public ApiResponse<LiveData<List<Movie>>> getPopularMovies() {
        if(cached.containsKey(POPULAR)) {
            return ApiResponse.success(cached.get(POPULAR));
        }

        errorMessage = null;

        if (hasExpired(POPULAR)) {
            tmdbService.getPopularMovies(BuildConfig.API_KEY).enqueue(new Callback<Movie[]>() {
                @Override
                public void onResponse(@NonNull Call<Movie[]> call, @NonNull Response<Movie[]> response) {
                    if (response.isSuccessful()) {
                        List<Movie> movies = Converter.toArrayList(response.body());

                        executor.execute(() -> {
                            HashSet<Integer> topRatedMovieIds = new HashSet(movieDao.getTopRatedMovieIds());
                            HashSet<Integer> favoriteMovieIds = new HashSet(movieDao.getFavoriteMovieIds());

                            for (Movie movie : movies) {
                                movie.posterPath = posterBaseUrl.concat(movie.posterPath);
                                movie.isPopular = true;
                                movie.isTopRated = topRatedMovieIds.contains(movie.id);
                                movie.isFavorite = favoriteMovieIds.contains(movie.id);
                            }

                            movieDao.clearPopularFlag();
                            movieDao.save(movies);

                            cached.put(POPULAR, movieDao.getPopularMovies());
                            lastCached.put(POPULAR, LocalDateTime.now());
                        });
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

        return errorMessage == null ? ApiResponse.success(movieDao.getPopularMovies()) : ApiResponse.failure(errorMessage);
    }

    public ApiResponse<LiveData<List<Movie>>> getTopRatedMovies() {
        if(cached.containsKey(TOP_RATED)) {
            return ApiResponse.success(cached.get(TOP_RATED));
        }

        errorMessage = null;

        if (hasExpired(TOP_RATED)) {
            tmdbService.getTopRatedMovies(BuildConfig.API_KEY).enqueue(new Callback<Movie[]>() {
                @Override
                public void onResponse(@NonNull Call<Movie[]> call, @NonNull Response<Movie[]> response) {
                    if (response.isSuccessful()) {
                        List<Movie> movies = Converter.toArrayList(response.body());


                        executor.execute(() -> {
                            HashSet<Integer> popularRatedMovieIds = new HashSet(movieDao.getPopularMovieIds());
                            HashSet<Integer> favoriteMovieIds = new HashSet(movieDao.getFavoriteMovieIds());

                            for (Movie movie : movies) {
                                movie.posterPath = posterBaseUrl.concat(movie.posterPath);
                                movie.isPopular = popularRatedMovieIds.contains(movie.id);
                                movie.isTopRated = true;
                                movie.isFavorite = favoriteMovieIds.contains(movie.id);
                            }

                            movieDao.clearTopRatedFlag();
                            movieDao.save(movies);

                            cached.put(TOP_RATED, movieDao.getTopRatedMovies());
                            lastCached.put(TOP_RATED, LocalDateTime.now());
                        });
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

        return errorMessage == null ? ApiResponse.success(movieDao.getTopRatedMovies()) : ApiResponse.failure(errorMessage);
    }

    private boolean hasExpired(MovieType movieType) {
        int MINUTES_TO_EXPIRE = 60;
        return !lastCached.containsKey(movieType) || MINUTES_TO_EXPIRE < ChronoUnit.MINUTES.between(lastCached.get(movieType), LocalDateTime.now());
    }
}