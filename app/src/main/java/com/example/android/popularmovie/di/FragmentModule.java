package com.example.android.popularmovie.di;

import com.example.android.popularmovie.OverviewFragment;
import com.example.android.popularmovie.ReviewFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract OverviewFragment contributeOverviewFragment();

    @ContributesAndroidInjector
    abstract ReviewFragment contributeReviewFragment();
}