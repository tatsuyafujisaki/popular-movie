package com.example.android.popularmovie.utils;

import android.arch.persistence.room.TypeConverter;

import java.time.LocalDate;

public class LocalDateConverter {
    @TypeConverter
    public static LocalDate toLocalDate(Long epochDay) {
        return epochDay == null ? null : LocalDate.ofEpochDay(epochDay);
    }

    @TypeConverter
    public static Long toLong(LocalDate date) {
        return date == null ? null : date.toEpochDay();
    }
}