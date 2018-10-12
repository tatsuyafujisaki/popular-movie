package com.example.android.popularmovie.room.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.example.android.popularmovie.BuildConfig;
import com.example.android.popularmovie.TmdbService;
import com.example.android.popularmovie.room.dao.ReviewDao;
import com.example.android.popularmovie.room.entity.Review;
import com.example.android.popularmovie.utils.ApiResponse;
import com.example.android.popularmovie.utils.Converter;
import com.example.android.popularmovie.utils.MyDateUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewRepository {
    private final TmdbService tmdbService;
    private final ReviewDao reviewDao;
    private final Executor executor;
    private String errorMessage;
    private final SparseArray<Long> lastUpdates = new SparseArray<>();

    public ReviewRepository(TmdbService tmdbService, ReviewDao reviewDao, Executor executor) {
        this.tmdbService = tmdbService;
        this.reviewDao = reviewDao;
        this.executor = executor;
    }

    public ApiResponse<LiveData<List<Review>>> getReviews(int movieId) {
        errorMessage = null;

        if (hasExpired(movieId)) {
            tmdbService.getReviews(movieId, BuildConfig.API_KEY).enqueue(new Callback<Review[]>() {
                @Override
                public void onResponse(@NonNull Call<Review[]> call, @NonNull Response<Review[]> response) {
                    if (response.isSuccessful()) {
                        List<Review> reviews = Converter.toArrayList(response.body());

                        for (Review review : reviews) {
                            review.movieId = movieId;
                        }

                        executor.execute(() -> {
                            reviewDao.save(reviews);
                            lastUpdates.put(movieId, System.currentTimeMillis());
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
                public void onFailure(@NonNull Call<Review[]> call, @NonNull Throwable t) {
                    errorMessage = t.getMessage();
                }
            });
        }

        return errorMessage == null ? ApiResponse.success(reviewDao.load(movieId)) : ApiResponse.failure(errorMessage);
    }

    private boolean hasExpired(int movieId) {
        int MINUTES_TO_EXPIRE = 60;
        Long lastUpdate = lastUpdates.get(movieId);
        return lastUpdate == null || MyDateUtils.Minute.hasExpired(lastUpdate, MINUTES_TO_EXPIRE);
    }
}