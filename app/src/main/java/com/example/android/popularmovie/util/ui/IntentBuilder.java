package com.example.android.popularmovie.util.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

public final class IntentBuilder {
    private final Intent intent;

    public IntentBuilder() {
        intent = new Intent();
    }

    public IntentBuilder(Context context, Class<?> cls) {
        intent = new Intent(context, cls);
    }

    public IntentBuilder putExtra(String key, int value) {
        intent.putExtra(key, value);
        return this;
    }

    public IntentBuilder putParcelable(String key, Parcelable value) {
        intent.putExtra(key, value);
        return this;
    }

    public Intent build() {
        return intent;
    }
}