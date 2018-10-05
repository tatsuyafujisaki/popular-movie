package com.example.android.popularmovie.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.example.android.popularmovie.DetailActivity;
import com.example.android.popularmovie.MainActivity;
import com.example.android.popularmovie.MainViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract DetailActivity contributeDetailActivity();

    @Provides
    static ViewModel provideViewModel(FragmentActivity activity, Class<MainViewModel> modelClass, ViewModelProvider.Factory factory) {
        return ViewModelProviders.of(activity, factory).get(modelClass);
    }
}