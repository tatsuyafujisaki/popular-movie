package com.example.android.popularmovie.util.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converter {
    public static <T> ArrayList<T> toArrayList(T[] xs) {
        return new ArrayList<>(Arrays.asList(xs));
    }

    public static <T> ArrayList<T> toArrayList(List<T> xs) {
        return (ArrayList<T>) xs;
    }
}