package com.example.android.popularmovie.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.android.popularmovie.room.dao.MovieDao;
import com.example.android.popularmovie.room.dao.ReviewDao;
import com.example.android.popularmovie.room.dao.TrailerDao;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.room.entity.Review;
import com.example.android.popularmovie.room.entity.Trailer;
import com.example.android.popularmovie.utils.LocalDateConverter;

@Database(entities = {Movie.class, Review.class, Trailer.class}, version = 1, exportSchema = false)
@TypeConverters(LocalDateConverter.class)
public abstract class MovieDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "Movies.db";
    private static MovieDatabase instance;

    public static MovieDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (MovieDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), MovieDatabase.class, MovieDatabase.DATABASE_NAME).build();
                }
            }
        }

        return instance;
    }

    public abstract MovieDao movieDao();
    public abstract TrailerDao trailerDao();
    public abstract ReviewDao reviewDao();
}