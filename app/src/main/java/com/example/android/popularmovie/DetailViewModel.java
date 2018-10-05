package com.example.android.popularmovie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovie.data.Review;
import com.example.android.popularmovie.data.ReviewRepository;
import com.example.android.popularmovie.utils.ApiResponse;

import java.util.List;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {
    private final ReviewRepository reviewRepository;

    @Inject
    public DetailViewModel(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    ApiResponse<LiveData<List<Review>>> getReviews(int movieId) {
        return reviewRepository.getReviews(movieId);
    }
}