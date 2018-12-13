package com.example.android.popularmovie.dagger.module;

import com.example.android.popularmovie.dagger.ViewModelFactory;
import com.example.android.popularmovie.dagger.ViewModelKey;
import com.example.android.popularmovie.viewmodel.MovieViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(MovieViewModel.class)
    abstract ViewModel bindMovieViewModel(MovieViewModel movieViewModel);
}