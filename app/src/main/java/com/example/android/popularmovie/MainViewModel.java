package com.example.android.popularmovie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovie.data.Movie;
import com.example.android.popularmovie.data.MovieRepository;
import com.example.android.popularmovie.utils.ApiResponse;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {
    private enum MovieType {POPULAR, TOP_RATED}

    private final MovieRepository movieRepository;
    private ApiResponse<LiveData<List<Movie>>> movies;
    private MovieType lastUpdatedMovieType;
    private LocalDateTime lastUpdate;

   @Inject
    public MainViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    ApiResponse<LiveData<List<Movie>>> getPopularMovies() {
        if (lastUpdatedMovieType != MovieType.POPULAR || hasExpired()) {
            movies = movieRepository.getPopularMovies();

            lastUpdatedMovieType = MovieType.POPULAR;
            lastUpdate = LocalDateTime.now();
        }

        return movies;
    }

    ApiResponse<LiveData<List<Movie>>> getTopRatedMovies() {
        if (lastUpdatedMovieType != MovieType.TOP_RATED || hasExpired()) {
            movies = movieRepository.getTopRatedMovies();

            lastUpdatedMovieType = MovieType.TOP_RATED;
            lastUpdate = LocalDateTime.now();
        }

        return movies;
    }

    private boolean hasExpired() {
        int MINUTES_TO_EXPIRE = 1;
        return lastUpdate == null || MINUTES_TO_EXPIRE < ChronoUnit.MINUTES.between(lastUpdate, LocalDateTime.now());
    }
}