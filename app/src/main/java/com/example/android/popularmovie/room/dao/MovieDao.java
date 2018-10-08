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
    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> load();

    @Query("UPDATE movie SET isPopular = 0")
    void ClearPopularFlag();

    @Query("UPDATE movie SET isTopRated = 0")
    void ClearTopRatedFlag();

    @Insert(onConflict = REPLACE)
    void save(List<Movie> movies);
}