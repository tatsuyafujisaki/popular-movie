package com.example.android.popularmovie.util.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.util.Objects;

public class IntentUtils {
    public static int getIntExtra(Intent intent, String key) {
        return intent.getIntExtra(key, Integer.MIN_VALUE);
    }

    public static <T extends Parcelable> T getParcelableExtra(Activity activity, String key) {
        return getIntent(activity).getParcelableExtra(key);
    }

    public static <T extends Parcelable> T getParcelableExtra(Fragment fragment, String key) {
        return getIntent(fragment).getParcelableExtra(key);
    }

    private static Intent getIntent(Activity activity) {
        return activity.getIntent();
    }

    private static Intent getIntent(Fragment fragment) {
        return getIntent(Objects.requireNonNull(fragment.getActivity()));
    }
}