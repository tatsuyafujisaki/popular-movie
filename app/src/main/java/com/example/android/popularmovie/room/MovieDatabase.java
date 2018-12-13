package com.example.android.popularmovie.room;

import android.content.Context;

import com.example.android.popularmovie.R;
import com.example.android.popularmovie.room.dao.MovieDao;
import com.example.android.popularmovie.room.dao.ReviewDao;
import com.example.android.popularmovie.room.dao.TrailerDao;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.room.entity.Review;
import com.example.android.popularmovie.room.entity.Trailer;
import com.example.android.popularmovie.util.DateConverter;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Movie.class, Trailer.class, Review.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class MovieDatabase extends RoomDatabase {
    private static MovieDatabase instance;

    public static MovieDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (MovieDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, MovieDatabase.class, context.getString(R.string.database_name)).build();
                }
            }
        }

        return instance;
    }

    public abstract MovieDao movieDao();
    public abstract TrailerDao trailerDao();
    public abstract ReviewDao reviewDao();
}