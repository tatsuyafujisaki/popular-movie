package com.example.android.popularmovie.di;

import android.arch.lifecycle.ViewModel;

import dagger.MapKey;

@SuppressWarnings("WeakerAccess")
@MapKey
public @interface ViewModelKey {
    @SuppressWarnings("unused")
    Class<? extends ViewModel> value();
}