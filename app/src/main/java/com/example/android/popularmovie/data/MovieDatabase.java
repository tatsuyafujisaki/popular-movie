package com.example.android.popularmovie.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class MovieDatabase extends RoomDatabase {
    private static MovieDatabase instance;
    private static final String DATABASE_NAME = "Movies.db";

    public static MovieDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (MovieDatabase.class) {
                instance = Room.databaseBuilder(context.getApplicationContext(), MovieDatabase.class, MovieDatabase.DATABASE_NAME).build();
            }
        }

        return instance;
    }

    public abstract MovieDao movieDao();
}