package com.example.android.popularmovie.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ReviewDao {
    @Query("SELECT * FROM review")
    LiveData<List<Review>> load();

    @Insert(onConflict = REPLACE)
    void save(List<Review> reviews);
}