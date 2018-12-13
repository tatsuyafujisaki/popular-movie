package com.example.android.popularmovie.room.dao;

import com.example.android.popularmovie.room.entity.Review;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ReviewDao {
    @Query("SELECT * FROM review WHERE movie_id = :movieId")
    LiveData<List<Review>> load(int movieId);

    @Insert(onConflict = REPLACE)
    void save(List<Review> reviews);
}