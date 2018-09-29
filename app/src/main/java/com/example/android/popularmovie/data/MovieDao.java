package com.example.android.popularmovie.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> load();

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<Movie> load(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(List<Movie> movies);
}