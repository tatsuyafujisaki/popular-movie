package com.example.android.popularmovie.room.dao;

import com.example.android.popularmovie.room.entity.Trailer;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TrailerDao {
    @Query("SELECT * FROM trailer WHERE movie_id = :movieId")
    LiveData<List<Trailer>> load(int movieId);

    @Insert(onConflict = REPLACE)
    void save(List<Trailer> trailers);
}