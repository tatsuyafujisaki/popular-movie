package com.example.android.popularmovie.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class ApplicationModule {
    @Singleton
    @Provides
    static Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    static String provideDatabaseName() {
        return "demo-dagger.db";
    }

    @Provides
    static Integer provideDatabaseVersion() {
        return 2;
    }
}