package com.example.android.popularmovie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovie.data.Movie;
import com.example.android.popularmovie.data.MovieRepository;
import com.example.android.popularmovie.utils.ApiResponse;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {
    private ApiResponse<LiveData<List<Movie>>> popularMovies;
    private ApiResponse<LiveData<List<Movie>>> topRatedMovies;
    private final MovieRepository movieRepository;

    @Inject
    public MainViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    ApiResponse<LiveData<List<Movie>>> getPopularMovies() {
        topRatedMovies = null;

        if (popularMovies == null) {
            popularMovies = movieRepository.getPopularMovies();
        }

        return popularMovies;
    }

    ApiResponse<LiveData<List<Movie>>> getTopRatedMovies() {
        popularMovies = null;

        if (topRatedMovies == null) {
            topRatedMovies = movieRepository.getTopRatedMovies();
        }

        return topRatedMovies;
    }
}