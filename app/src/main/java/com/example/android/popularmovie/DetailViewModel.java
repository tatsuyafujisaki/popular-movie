package com.example.android.popularmovie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.SparseArray;

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
    private final SparseArray<LocalDateTime> lastUpdates = new SparseArray<>();

    @Inject
    public DetailViewModel(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    ApiResponse<LiveData<List<Review>>> getReviews(int movieId) {
        if (hasExpired(movieId)) {
            reviews = reviewRepository.getReviews(movieId);
            lastUpdates.put(movieId, LocalDateTime.now());
        }

        return reviews;
    }

    private boolean hasExpired(int movieId) {
        int MINUTES_TO_EXPIRE = 60;

        LocalDateTime lastUpdate = lastUpdates.get(movieId);

        return lastUpdate == null || MINUTES_TO_EXPIRE < ChronoUnit.MINUTES.between(lastUpdate, LocalDateTime.now());
    }
}