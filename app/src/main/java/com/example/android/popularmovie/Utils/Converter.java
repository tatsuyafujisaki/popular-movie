package com.example.android.popularmovie.Utils;

import java.util.ArrayList;
import java.util.Arrays;

public class Converter {
    public static <T> ArrayList<T> toArrayList(T[] xs) {
        return new ArrayList<>(Arrays.asList(xs));
    }
}
