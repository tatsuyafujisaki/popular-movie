package com.example.android.popularmovie.di;

import com.example.android.popularmovie.DetailFragment;
import com.example.android.popularmovie.MainFragment;
import com.example.android.popularmovie.TabFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract MainFragment contributeMainFragment();

    @ContributesAndroidInjector
    abstract DetailFragment contributeDetailFragment();

    @ContributesAndroidInjector
    abstract TabFragment contributeTabFragment();
}