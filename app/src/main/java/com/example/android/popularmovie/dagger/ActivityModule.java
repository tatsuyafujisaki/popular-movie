package com.example.android.popularmovie.dagger;

import com.example.android.popularmovie.DetailActivity;
import com.example.android.popularmovie.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract DetailActivity contributeDetailActivity();
}