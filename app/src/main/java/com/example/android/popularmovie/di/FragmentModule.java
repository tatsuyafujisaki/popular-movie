package com.example.android.popularmovie.di;

import com.example.android.popularmovie.OverviewFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract OverviewFragment contributeTabFragment();
}