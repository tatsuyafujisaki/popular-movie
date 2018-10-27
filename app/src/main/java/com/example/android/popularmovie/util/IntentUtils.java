package com.example.android.popularmovie.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.Objects;

public final class IntentUtils {
    private static final String INTENT_EXTRA_KEY = "INTENT_EXTRA_KEY";

    public static int getIntExtra(@NonNull Intent intent) {
        return intent.getIntExtra(INTENT_EXTRA_KEY, Integer.MIN_VALUE);
    }

    public static <T extends Parcelable> T getParcelableExtra(@NonNull Activity activity) {
        return activity.getIntent().getParcelableExtra(INTENT_EXTRA_KEY);
    }

    public static <T extends Parcelable> T getParcelableExtra(@NonNull Fragment fragment) {
        return getParcelableExtra(Objects.requireNonNull(fragment.getActivity()));
    }

    public static Intent createIntent(int value) {
        return new Intent().putExtra(INTENT_EXTRA_KEY, value);
    }

    public static Intent createIntent(Context packageContext, Class<?> cls, Parcelable value) {
        return new Intent(packageContext, cls).putExtra(INTENT_EXTRA_KEY, value);
    }
}