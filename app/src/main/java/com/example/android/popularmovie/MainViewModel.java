package com.example.android.popularmovie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovie.data.Movie;
import com.example.android.popularmovie.data.MovieRepository;
import com.example.android.popularmovie.utils.ApiResponse;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {
    private final MovieRepository movieRepository;

   @Inject
    public MainViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    ApiResponse<LiveData<List<Movie>>> getPopularMovies() {
        return movieRepository.getPopularMovies();
    }

    ApiResponse<LiveData<List<Movie>>> getTopRatedMovies() {
        return movieRepository.getTopRatedMovies();
    }
}