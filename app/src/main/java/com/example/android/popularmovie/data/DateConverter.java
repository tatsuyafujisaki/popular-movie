package com.example.android.popularmovie.data;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long dateInLong) {
        return dateInLong == null ? null : new Date(dateInLong);
    }

    @TypeConverter
    public static Long toLong(Date date) {
        return date == null ? null : date.getTime();
    }
}