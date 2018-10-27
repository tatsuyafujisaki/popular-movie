package com.example.android.popularmovie.util;

import java.util.Calendar;

public class MyDateUtils {
    public static class Day {
        public static boolean hasExpired(long dateInMillis, int daysToExpire) {
            return addDays(dateInMillis, daysToExpire) < System.currentTimeMillis();
        }
    }

    public static class Minute {
        public static boolean hasExpired(long dateInMillis, int minutesToExpire) {
            return addMinutes(dateInMillis, minutesToExpire) < System.currentTimeMillis();
        }
    }

    private static long addDays(long dateInMillis, int daysToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);
        calendar.add(Calendar.DATE, daysToAdd);
        return calendar.getTimeInMillis();
    }

    private static long addMinutes(long dateInMillis, int minutesToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);
        calendar.add(Calendar.MINUTE, minutesToAdd);
        return calendar.getTimeInMillis();
    }
}