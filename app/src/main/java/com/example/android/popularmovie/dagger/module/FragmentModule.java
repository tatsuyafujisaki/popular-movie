package com.example.android.popularmovie.dagger.module;

import com.example.android.popularmovie.ui.fragment.OverviewFragment;
import com.example.android.popularmovie.ui.fragment.ReviewFragment;
import com.example.android.popularmovie.ui.fragment.TrailerFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract OverviewFragment contributeOverviewFragment();

    @ContributesAndroidInjector
    abstract TrailerFragment contributeTrailerFragment();

    @ContributesAndroidInjector
    abstract ReviewFragment contributeReviewFragment();
}