package com.example.android.popularmovie.util;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import java.util.Date;

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