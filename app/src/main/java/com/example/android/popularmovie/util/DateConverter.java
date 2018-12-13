package com.example.android.popularmovie.util;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

public class DateConverter {
    @TypeConverter
    public static Date toDate(@NonNull Long value) {
        return new Date(value);
    }

    @TypeConverter
    public static Long toLong(@NonNull Date date) {
        return date.getTime();
    }
}