package com.example.android.popularmovie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovie.data.Movie;
import com.example.android.popularmovie.data.MovieRepository;
import com.example.android.popularmovie.data.Review;
import com.example.android.popularmovie.data.ReviewRepository;
import com.example.android.popularmovie.data.Trailer;
import com.example.android.popularmovie.data.TrailerRepository;
import com.example.android.popularmovie.utils.ApiResponse;

import java.util.List;

import javax.inject.Inject;

public class MovieViewModel extends ViewModel {
    private final MovieRepository movieRepository;
    private final TrailerRepository trailerRepository;
    private final ReviewRepository reviewRepository;

   @Inject
    public MovieViewModel(MovieRepository movieRepository, TrailerRepository trailerRepository, ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.trailerRepository = trailerRepository;
        this.reviewRepository = reviewRepository;
   }

    ApiResponse<LiveData<List<Movie>>> getPopularMovies() {
        return movieRepository.getPopularMovies();
    }

    ApiResponse<LiveData<List<Movie>>> getTopRatedMovies() {
        return movieRepository.getTopRatedMovies();
    }

    ApiResponse<LiveData<List<Trailer>>> getTrailers(int movieId) {
        return trailerRepository.getTrailers(movieId);
    }

    ApiResponse<LiveData<List<Review>>> getReviews(int movieId) {
        return reviewRepository.getReviews(movieId);
    }
}