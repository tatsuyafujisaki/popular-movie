package com.example.android.popularmovie.dagger.module;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;

import com.example.android.popularmovie.R;
import com.example.android.popularmovie.room.MovieDatabase;
import com.example.android.popularmovie.room.dao.MovieDao;
import com.example.android.popularmovie.room.dao.ReviewDao;
import com.example.android.popularmovie.room.dao.TrailerDao;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.room.entity.Review;
import com.example.android.popularmovie.room.entity.Trailer;
import com.example.android.popularmovie.room.repository.MovieRepository;
import com.example.android.popularmovie.room.repository.ReviewRepository;
import com.example.android.popularmovie.room.repository.TrailerRepository;
import com.example.android.popularmovie.util.TmdbService;
import com.example.android.popularmovie.viewmodel.MovieViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {
    @Singleton
    @Provides
    static Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    static Resources provideResources(Context context) {
        return context.getResources();
    }

    @Singleton
    @Provides
    static MovieDatabase provideMovieDatabase(Context context) {
        return MovieDatabase.getInstance(context);
    }

    @Singleton
    @Provides
    static MovieDao provideMovieDao(MovieDatabase movieDatabase) {
        return movieDatabase.movieDao();
    }

    @Singleton
    @Provides
    static TrailerDao provideTrailerDao(MovieDatabase movieDatabase) {
        return movieDatabase.trailerDao();
    }

    @Singleton
    @Provides
    static ReviewDao provideReviewDao(MovieDatabase movieDatabase) {
        return movieDatabase.reviewDao();
    }

    @Singleton
    @Provides
    static ViewModel provideViewModel(FragmentActivity activity, Class<MovieViewModel> modelClass, ViewModelProvider.Factory factory) {
        return ViewModelProviders.of(activity, factory).get(modelClass);
    }

    @Singleton
    @Provides
    @Named("TmdbJsonResultElement")
    static String provideTmdbJsonResultElement(Context context) {
        return context.getString(R.string.tmdb_json_result_element);
    }

    @Singleton
    @Provides
    @Named("TmdbBaseUrl")
    static String provideTmdbBaseUrl(Context context) {
        return context.getString(R.string.tmdb_base_url);
    }

    @Singleton
    @Provides
    @Named("GsonWithMovieArrayAdapter")
    static Gson provideGsonWithMovieArrayAdapter(@Named("TmdbJsonResultElement") String jsonResultElement) {
        return new GsonBuilder().registerTypeAdapter(new TypeToken<List<Movie>>() {
        }.getType(), (JsonDeserializer<List<Movie>>) (json, type, context1)
                -> new Gson().fromJson(json.getAsJsonObject().getAsJsonArray(jsonResultElement), type)).create();
    }

    @Singleton
    @Provides
    @Named("GsonWithTrailerArrayAdapter")
    static Gson provideGsonWithTrailerArrayAdapter(@Named("TmdbJsonResultElement") String jsonResultElement) {
        return new GsonBuilder().registerTypeAdapter(new TypeToken<List<Trailer>>() {
        }.getType(), (JsonDeserializer<List<Trailer>>) (json, type, context1)
                -> new Gson().fromJson(json.getAsJsonObject().getAsJsonArray(jsonResultElement), type)).create();
    }

    @Singleton
    @Provides
    @Named("GsonWithReviewArrayAdapter")
    static Gson provideGsonWithReviewArrayAdapter(@Named("TmdbJsonResultElement") String jsonResultElement) {
        return new GsonBuilder().registerTypeAdapter(new TypeToken<List<Review>>() {
        }.getType(), (JsonDeserializer<List<Review>>) (json, type, context1)
                -> new Gson().fromJson(json.getAsJsonObject().getAsJsonArray(jsonResultElement), type)).create();
    }

    @Singleton
    @Provides
    @Named("TmdbServiceWithMovieArrayAdapter")
    static TmdbService provideTmdbServiceWithMovieArrayAdapter(@Named("GsonWithMovieArrayAdapter") Gson gson, @Named("TmdbBaseUrl") String tmdbBaseUrl) {
        return new Retrofit.Builder()
                .baseUrl(tmdbBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TmdbService.class);
    }

    @Singleton
    @Provides
    @Named("TmdbServiceWithTrailerArrayAdapter")
    static TmdbService provideTmdbServiceWithTrailerArrayAdapter(@Named("GsonWithTrailerArrayAdapter") Gson gson, @Named("TmdbBaseUrl") String tmdbBaseUrl) {
        return new Retrofit.Builder()
                .baseUrl(tmdbBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TmdbService.class);
    }

    @Singleton
    @Provides
    @Named("TmdbServiceWithReviewArrayAdapter")
    static TmdbService provideTmdbServiceWithReviewArrayAdapter(@Named("GsonWithReviewArrayAdapter") Gson gson, @Named("TmdbBaseUrl") String tmdbBaseUrl) {
        return new Retrofit.Builder()
                .baseUrl(tmdbBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TmdbService.class);
    }

    @Singleton
    @Provides
    static MovieRepository provideMovieRepository(Context context,
                                                  @Named("TmdbServiceWithMovieArrayAdapter") TmdbService tmdbService,
                                                  MovieDao movieDao) {
        return new MovieRepository(tmdbService, movieDao, context.getString(R.string.tmdb_image_base_url));
    }

    @Singleton
    @Provides
    static TrailerRepository provideTrailerRepository(@Named("TmdbServiceWithTrailerArrayAdapter") TmdbService tmdbService, TrailerDao trailerDao) {
        return new TrailerRepository(tmdbService, trailerDao);
    }

    @Singleton
    @Provides
    static ReviewRepository provideReviewRepository(@Named("TmdbServiceWithReviewArrayAdapter") TmdbService tmdbService, ReviewDao reviewDao) {
        return new ReviewRepository(tmdbService, reviewDao);
    }

    @Singleton
    @Provides
    static int provideGridColumnSpan(Resources resources) {
        return Math.max(1, resources.getDisplayMetrics().widthPixels / resources.getInteger(R.integer.grid_column_span));
    }
}