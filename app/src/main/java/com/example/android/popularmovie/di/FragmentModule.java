package com.example.android.popularmovie.di;

import com.example.android.popularmovie.TabFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract TabFragment contributeTabFragment();
}