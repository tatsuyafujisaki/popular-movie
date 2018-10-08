package com.example.android.popularmovie.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.popularmovie.room.entity.Trailer;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TrailerDao {
    @Query("SELECT * FROM trailer WHERE movie_id = :movieId")
    LiveData<List<Trailer>> load(int movieId);

    @Insert(onConflict = REPLACE)
    void save(List<Trailer> trailers);
}