package com.example.android.popularmovie.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.SparseArray;

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
    private final TmdbService tmdbService;
    private final ReviewDao reviewDao;
    private final Executor executor;
    private String errorMessage;
    private final SparseArray<LiveData<List<Review>>> cached = new SparseArray<>();
    private final SparseArray<LocalDateTime> lastCached = new SparseArray<>();

    public ReviewRepository(TmdbService tmdbService, ReviewDao reviewDao, Executor executor) {
        this.tmdbService = tmdbService;
        this.reviewDao = reviewDao;
        this.executor = executor;
    }

    public ApiResponse<LiveData<List<Review>>> getReviews(int movieId) {
        if(cached.get(movieId) != null) {
            return ApiResponse.success(cached.get(movieId));
        }

        errorMessage = null;

        if (hasExpired(movieId)) {
            tmdbService.getReviews(movieId, BuildConfig.API_KEY).enqueue(new Callback<Review[]>() {
                @Override
                public void onResponse(@NonNull Call<Review[]> call, @NonNull Response<Review[]> response) {
                    if (response.isSuccessful()) {
                        List<Review> reviews = Converter.toArrayList(response.body());

                        reviews.forEach(review -> review.movieId = movieId);

                        executor.execute(() -> reviewDao.save(reviews));

                        cached.put(movieId, reviewDao.load(movieId));
                        lastCached.put(movieId, LocalDateTime.now());
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

        LocalDateTime lastCachedTime = lastCached.get(movieId);

        return lastCachedTime == null || MINUTES_TO_EXPIRE < ChronoUnit.MINUTES.between(lastCachedTime, LocalDateTime.now());
    }
}