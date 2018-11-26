package com.example.android.popularmovie.room.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.ArraySet;

import com.example.android.popularmovie.BuildConfig;
import com.example.android.popularmovie.room.dao.MovieDao;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.util.ApiResponse;
import com.example.android.popularmovie.util.MyDateUtils;
import com.example.android.popularmovie.util.TmdbService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.popularmovie.room.repository.MovieRepository.MovieType.POPULAR;
import static com.example.android.popularmovie.room.repository.MovieRepository.MovieType.TOP_RATED;

public class MovieRepository {
    public enum MovieType {POPULAR, TOP_RATED, FAVORITE}

    private final TmdbService tmdbService;
    private final MovieDao movieDao;
    private final String posterBaseUrl;
    private String errorMessage;

    private final Map<MovieType, Long> lastUpdates = new ArrayMap<>();

    public MovieRepository(TmdbService tmdbService, MovieDao movieDao, String posterBaseUrl) {
        this.tmdbService = tmdbService;
        this.movieDao = movieDao;
        this.posterBaseUrl = posterBaseUrl;
    }

    public ApiResponse<LiveData<List<Movie>>> getMovies(MovieType movieType) {
        switch (movieType) {
            case POPULAR:
                return getPopularMovies();
            case TOP_RATED:
                return getTopRatedMovies();
            case FAVORITE:
                return getFavoriteMovies();
            default:
                throw new IllegalArgumentException(movieType.toString());
        }
    }

    private ApiResponse<LiveData<List<Movie>>> getPopularMovies() {
        errorMessage = null;

        if (hasExpired(POPULAR)) {
            tmdbService.getPopularMovies(BuildConfig.TMDB_API_KEY).enqueue(new Callback<List<Movie>>() {
                @Override
                public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                    if (response.isSuccessful()) {
                        List<Movie> movies = response.body();

                        AsyncTask.execute(() -> {
                            Set<Integer> topRatedMovieIds = new ArraySet<>(movieDao.getTopRatedMovieIds());
                            Set<Integer> favoriteMovieIds = new ArraySet<>(movieDao.getFavoriteMovieIds());

                            for (Movie movie : Objects.requireNonNull(movies)) {
                                movie.posterPath = posterBaseUrl.concat(movie.posterPath);
                                movie.isPopular = true;
                                movie.isTopRated = topRatedMovieIds.contains(movie.id);
                                movie.isFavorite = favoriteMovieIds.contains(movie.id);
                            }

                            // Delete previously popular movies unless they are top rated or favorite
                            movieDao.deleteIfNotTopRatedNorFavorite();
                            movieDao.save(movies);
                        });

                        lastUpdates.put(POPULAR, System.currentTimeMillis());
                    } else {
                        try {
                            errorMessage = Objects.requireNonNull(response.errorBody()).string();
                        } catch (IOException e) {
                            errorMessage = e.getMessage();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                    errorMessage = t.getMessage();
                }
            });
        }

        return errorMessage == null ? ApiResponse.success(movieDao.getPopularMovies()) : ApiResponse.failure(errorMessage);
    }

    private ApiResponse<LiveData<List<Movie>>> getTopRatedMovies() {
        errorMessage = null;

        if (hasExpired(TOP_RATED)) {
            tmdbService.getTopRatedMovies(BuildConfig.TMDB_API_KEY).enqueue(new Callback<List<Movie>>() {
                @Override
                public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                    if (response.isSuccessful()) {
                        List<Movie> movies = response.body();

                        AsyncTask.execute(() -> {
                            Set<Integer> popularRatedMovieIds = new ArraySet<>(movieDao.getPopularMovieIds());
                            Set<Integer> favoriteMovieIds = new ArraySet<>(movieDao.getFavoriteMovieIds());

                            for (Movie movie : Objects.requireNonNull(movies)) {
                                movie.posterPath = posterBaseUrl.concat(movie.posterPath);
                                movie.isPopular = popularRatedMovieIds.contains(movie.id);
                                movie.isTopRated = true;
                                movie.isFavorite = favoriteMovieIds.contains(movie.id);
                            }

                            // Delete previously top rated movies unless they are popular or favorite
                            movieDao.deleteIfNotPopularNorFavorite();
                            movieDao.save(movies);
                        });

                        lastUpdates.put(TOP_RATED, System.currentTimeMillis());
                    } else {
                        try {
                            errorMessage = Objects.requireNonNull(response.errorBody()).string();
                        } catch (IOException e) {
                            errorMessage = e.getMessage();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                    errorMessage = t.getMessage();
                }
            });
        }

        return errorMessage == null ? ApiResponse.success(movieDao.getTopRatedMovies()) : ApiResponse.failure(errorMessage);
    }

    private ApiResponse<LiveData<List<Movie>>> getFavoriteMovies() {
        return ApiResponse.success(movieDao.getFavoriteMovies());
    }

    public void updateFavorite(int movieId, boolean isFavorite) {
        movieDao.updateFavorite(movieId, isFavorite);
    }

    private boolean hasExpired(MovieType movieType) {
        final int MINUTES_TO_EXPIRE = 60;
        return !lastUpdates.containsKey(movieType) || MyDateUtils.Minute.hasExpired(lastUpdates.get(movieType), MINUTES_TO_EXPIRE);
    }
}