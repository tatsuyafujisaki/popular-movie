package com.example.android.popularmovie.di;

import com.example.android.popularmovie.DetailFragment;
import com.example.android.popularmovie.MainFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract MainFragment contributeMainFragment();

    @ContributesAndroidInjector
    abstract DetailFragment contributeDetailFragment();
}