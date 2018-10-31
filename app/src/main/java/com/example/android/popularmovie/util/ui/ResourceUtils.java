package com.example.android.popularmovie.util.ui;

import android.content.res.Resources;

public class ResourceUtils {
    public static int getGridColumnSpan(Resources resources, int gridColumnWidthResId) {
        return Math.max(1, resources.getDisplayMetrics().widthPixels / resources.getInteger(gridColumnWidthResId));
    }
}