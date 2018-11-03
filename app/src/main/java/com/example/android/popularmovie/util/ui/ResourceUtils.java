package com.example.android.popularmovie.util.ui;

import android.app.Activity;
import android.content.res.Resources;

public class ResourceUtils {
    public static int getGridColumnSpan(Activity activity, int gridColumnWidthResId) {
        Resources resources = activity.getResources();
        return Math.max(1, resources.getDisplayMetrics().widthPixels / resources.getInteger(gridColumnWidthResId));
    }

    public static int getInteger(Activity activity, int id) {
        return activity.getResources().getInteger(id);
    }
}