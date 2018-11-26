package com.example.android.popularmovie.room.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.example.android.popularmovie.BuildConfig;
import com.example.android.popularmovie.room.dao.TrailerDao;
import com.example.android.popularmovie.room.entity.Trailer;
import com.example.android.popularmovie.util.ApiResponse;
import com.example.android.popularmovie.util.MyDateUtils;
import com.example.android.popularmovie.util.TmdbService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailerRepository {
    private final TmdbService tmdbService;
    private final TrailerDao TrailerDao;
    private String errorMessage;
    private final SparseArray<Long> lastUpdates = new SparseArray<>();

    public TrailerRepository(TmdbService tmdbService, com.example.android.popularmovie.room.dao.TrailerDao TrailerDao) {
        this.tmdbService = tmdbService;
        this.TrailerDao = TrailerDao;
    }

    public ApiResponse<LiveData<List<Trailer>>> getTrailers(int movieId) {
        errorMessage = null;

        if (hasExpired(movieId)) {
            tmdbService.getTrailers(movieId, BuildConfig.TMDB_API_KEY).enqueue(new Callback<List<Trailer>>() {
                @Override
                public void onResponse(@NonNull Call<List<Trailer>> call, @NonNull Response<List<Trailer>> response) {
                    if (response.isSuccessful()) {
                        List<Trailer> trailers = response.body();

                        for (Trailer trailer : Objects.requireNonNull(trailers)) {
                            trailer.movieId = movieId;
                        }

                        AsyncTask.execute(() -> {
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
                public void onFailure(@NonNull Call<List<Trailer>> call, @NonNull Throwable t) {
                    errorMessage = t.getMessage();
                }
            });
        }

        return errorMessage == null ? ApiResponse.success(TrailerDao.load(movieId)) : ApiResponse.failure(errorMessage);
    }

    private boolean hasExpired(int movieId) {
        final int DAYS_TO_EXPIRE = 1;
        Long lastUpdate = lastUpdates.get(movieId);
        return lastUpdate == null || MyDateUtils.Day.hasExpired(lastUpdate, DAYS_TO_EXPIRE);
    }
}