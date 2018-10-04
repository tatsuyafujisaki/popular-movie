package com.example.android.popularmovie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovie.data.Review;
import com.example.android.popularmovie.data.ReviewRepository;
import com.example.android.popularmovie.utils.ApiResponse;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {
    private final ReviewRepository reviewRepository;
    private ApiResponse<LiveData<List<Review>>> reviews;
    private LocalDateTime lastUpdate;

    @Inject
    public DetailViewModel(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    ApiResponse<LiveData<List<Review>>> getReviews(int movieId) {
        if (hasExpired()) {
            reviews = reviewRepository.getReviews(movieId);
            lastUpdate = LocalDateTime.now();
        }

        return reviews;
    }

    private boolean hasExpired() {
        int MINUTES_TO_EXPIRE = 10;
        return lastUpdate == null || MINUTES_TO_EXPIRE < ChronoUnit.MINUTES.between(lastUpdate, LocalDateTime.now());
    }
}