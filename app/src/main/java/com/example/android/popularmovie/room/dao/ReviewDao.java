package com.example.android.popularmovie.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.popularmovie.room.entity.Review;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ReviewDao {
    @Query("SELECT * FROM review WHERE movie_id = :movieId")
    LiveData<List<Review>> load(int movieId);

    @Insert(onConflict = REPLACE)
    void save(List<Review> reviews);
}