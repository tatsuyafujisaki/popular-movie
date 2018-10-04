package com.example.android.popularmovie.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.popularmovie.BuildConfig;
import com.example.android.popularmovie.TmdbService;
import com.example.android.popularmovie.utils.ApiResponse;
import com.example.android.popularmovie.utils.Converter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewRepository {
    private LocalDateTime lastUpdate;

    private final TmdbService tmdbService;
    private final ReviewDao reviewDao;
    private final Executor executor;
    private String errorMessage;

    public ReviewRepository(TmdbService tmdbService, ReviewDao reviewDao, Executor executor) {
        this.tmdbService = tmdbService;
        this.reviewDao = reviewDao;
        this.executor = executor;
    }

    public ApiResponse<LiveData<List<Review>>> getReviews(int movieId) {
        errorMessage = null;

        if (hasExpired()) {
            tmdbService.getReviews(BuildConfig.API_KEY, movieId).enqueue(new Callback<Review[]>() {
                @Override
                public void onResponse(@NonNull Call<Review[]> call, @NonNull Response<Review[]> response) {
                    if (response.isSuccessful()) {
                        List<Review> reviews = Converter.toArrayList(response.body());

                        executor.execute(() -> reviewDao.save(reviews));
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

            lastUpdate = LocalDateTime.now();
        }

        return errorMessage == null ? ApiResponse.success(reviewDao.load()) : ApiResponse.failure(errorMessage);
    }

    private boolean hasExpired() {
        int MINUTES_TO_EXPIRE = 1;
        return lastUpdate == null || MINUTES_TO_EXPIRE < ChronoUnit.MINUTES.between(lastUpdate, LocalDateTime.now());
    }
}