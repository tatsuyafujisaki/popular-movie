package com.example.android.popularmovie.room.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.example.android.popularmovie.BuildConfig;
import com.example.android.popularmovie.TmdbService;
import com.example.android.popularmovie.room.dao.TrailerDao;
import com.example.android.popularmovie.room.entity.Trailer;
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

public class TrailerRepository {
    private final TmdbService tmdbService;
    private final TrailerDao TrailerDao;
    private final Executor executor;
    private String errorMessage;
    private final SparseArray<LocalDateTime> lastUpdates = new SparseArray<>();

    public TrailerRepository(TmdbService tmdbService, com.example.android.popularmovie.room.dao.TrailerDao TrailerDao, Executor executor) {
        this.tmdbService = tmdbService;
        this.TrailerDao = TrailerDao;
        this.executor = executor;
    }

    public ApiResponse<LiveData<List<Trailer>>> getTrailers(int movieId) {
        errorMessage = null;

        if (hasExpired(movieId)) {
            tmdbService.getTrailers(movieId, BuildConfig.API_KEY).enqueue(new Callback<Trailer[]>() {
                @Override
                public void onResponse(@NonNull Call<Trailer[]> call, @NonNull Response<Trailer[]> response) {
                    if (response.isSuccessful()) {
                        List<Trailer> Trailers = Converter.toArrayList(response.body());

                        Trailers.forEach(Trailer -> Trailer.movieId = movieId);

                        executor.execute(() -> TrailerDao.save(Trailers));

                        lastUpdates.put(movieId, LocalDateTime.now());
                    } else {
                        try {
                            errorMessage = Objects.requireNonNull(response.errorBody()).string();
                        } catch (IOException e) {
                            errorMessage = e.getMessage();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Trailer[]> call, @NonNull Throwable t) {
                    errorMessage = t.getMessage();
                }
            });
        }

        return errorMessage == null ? ApiResponse.success(TrailerDao.load(movieId)) : ApiResponse.failure(errorMessage);
    }

    private boolean hasExpired(int movieId) {
        int DAYS_TO_EXPIRE = 1;

        LocalDateTime lastCachedTime = lastUpdates.get(movieId);

        return lastCachedTime == null || DAYS_TO_EXPIRE < ChronoUnit.DAYS.between(lastCachedTime, LocalDateTime.now());
    }
}