package com.example.android.popularmovie.dagger;

import androidx.lifecycle.ViewModel;

import dagger.MapKey;

@MapKey
public @interface ViewModelKey {
    Class<? extends ViewModel> value();
}