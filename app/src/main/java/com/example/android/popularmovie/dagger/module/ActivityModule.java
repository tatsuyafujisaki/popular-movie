package com.example.android.popularmovie.dagger.module;

import com.example.android.popularmovie.ui.activity.DetailActivity;
import com.example.android.popularmovie.ui.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract DetailActivity contributeDetailActivity();
}