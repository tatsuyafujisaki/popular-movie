package com.example.android.popularmovie.util.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;

import java.io.Serializable;

public final class BundleUtils {
    private static final String KEY = null;

    public static boolean getBoolean(@NonNull Bundle bundle) {
        return bundle.getBoolean(KEY);
    }

    public static <T> T get(@NonNull Bundle bundle) {
        return (T) bundle.get(KEY);
    }

    public static void putBoolean(@NonNull Bundle bundle, boolean value) {
        bundle.putBoolean(KEY, value);
    }

    public static void putSerializable(@NonNull Bundle bundle, Serializable value) {
        bundle.putSerializable(KEY, value);
    }
}