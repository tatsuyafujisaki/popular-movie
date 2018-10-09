package com.example.android.popularmovie.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.popularmovie.room.entity.Movie;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie WHERE is_popular = 1")
    LiveData<List<Movie>> getPopularMovies();

    @Query("SELECT * FROM movie WHERE is_top_rated = 1")
    LiveData<List<Movie>> getTopRatedMovies();

    @Query("SELECT * FROM movie WHERE is_favorite = 1")
    LiveData<List<Movie>> getFavoriteMovies();

    @Query("SELECT id FROM movie WHERE is_popular = 1")
    List<Integer> getPopularMovieIds();

    @Query("SELECT id FROM movie WHERE is_top_rated = 1")
    List<Integer> getTopRatedMovieIds();

    @Query("SELECT id FROM movie WHERE is_favorite = 1")
    List<Integer> getFavoriteMovieIds();

    @Query("DELETE FROM movie WHERE is_top_rated = 0 AND is_favorite = 0")
    void deleteIfNotTopRatedNorFavorite();

    @Query("DELETE FROM movie WHERE is_popular = 0 AND is_favorite = 0")
    void deleteIfNotPopularNorFavorite();

    @Insert(onConflict = REPLACE)
    void save(List<Movie> movies);
}