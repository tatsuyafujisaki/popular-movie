package com.example.android.popularmovie.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.android.popularmovie.R;
import com.example.android.popularmovie.TmdbService;
import com.example.android.popularmovie.utils.ApiResponse;
import com.example.android.popularmovie.utils.LocalDateDeserializer;
import com.example.android.popularmovie.utils.MoviesDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieViewModel extends AndroidViewModel {
    private ApiResponse<LiveData<List<Movie>>> popularMovies;
    private ApiResponse<LiveData<List<Movie>>> topRatedMovies;
    private final MovieRepository movieRepository;

    MovieViewModel(Application application) {
        super(application);
        Context context = application.getApplicationContext();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Movie[].class, new MoviesDeserializer(context.getString(R.string.tmdb_json_results_element)))
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .create();

        TmdbService tmdbService = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.tmdb_base_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TmdbService.class);

        movieRepository = new MovieRepository(
                tmdbService,
                MovieDatabase.getInstance(context).movieDao(),
                Executors.newSingleThreadExecutor(),
                context.getString(R.string.poster_base_url));
    }

    public ApiResponse<LiveData<List<Movie>>> getPopularMovies() {
        if (popularMovies == null) {
            popularMovies = movieRepository.getPopularMovies();
        }

        return popularMovies;
    }

    public ApiResponse<LiveData<List<Movie>>> getTopRatedMovies() {
        if (topRatedMovies == null) {
            // TODO: FIX LATER
            topRatedMovies = movieRepository.getPopularMovies();
        }

        return topRatedMovies;
    }
}