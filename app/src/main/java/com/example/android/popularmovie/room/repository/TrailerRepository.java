package com.example.android.popularmovie.room.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.example.android.popularmovie.BuildConfig;
import com.example.android.popularmovie.util.TmdbService;
import com.example.android.popularmovie.room.dao.TrailerDao;
import com.example.android.popularmovie.room.entity.Trailer;
import com.example.android.popularmovie.util.ApiResponse;
import com.example.android.popularmovie.util.Converter;
import com.example.android.popularmovie.util.MyDateUtils;

import java.io.IOException;
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
    private final SparseArray<Long> lastUpdates = new SparseArray<>();

    public TrailerRepository(TmdbService tmdbService, com.example.android.popularmovie.room.dao.TrailerDao TrailerDao, Executor executor) {
        this.tmdbService = tmdbService;
        this.TrailerDao = TrailerDao;
        this.executor = executor;
    }

    public ApiResponse<LiveData<List<Trailer>>> getTrailers(int movieId) {
        errorMessage = null;

        if (hasExpired(movieId)) {
            tmdbService.getTrailers(movieId, BuildConfig.TMDB_API_KEY).enqueue(new Callback<Trailer[]>() {
                @Override
                public void onResponse(@NonNull Call<Trailer[]> call, @NonNull Response<Trailer[]> response) {
                    if (response.isSuccessful()) {
                        List<Trailer> trailers = Converter.toArrayList(response.body());

                        for (Trailer trailer : trailers) {
                            trailer.movieId = movieId;
                        }

                        executor.execute(() -> {
                            TrailerDao.save(trailers);
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
                public void onFailure(@NonNull Call<Trailer[]> call, @NonNull Throwable t) {
                    errorMessage = t.getMessage();
                }
            });
        }

        return errorMessage == null ? ApiResponse.success(TrailerDao.load(movieId)) : ApiResponse.failure(errorMessage);
    }

    private boolean hasExpired(int movieId) {
        int daysToExpire = 1;
        Long lastUpdate = lastUpdates.get(movieId);
        return lastUpdate == null || MyDateUtils.Day.hasExpired(lastUpdate, daysToExpire);
    }
}