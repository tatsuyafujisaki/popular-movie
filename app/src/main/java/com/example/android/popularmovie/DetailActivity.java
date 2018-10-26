package com.example.android.popularmovie;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.android.popularmovie.adapter.MyFragmentPagerAdapter;
import com.example.android.popularmovie.databinding.ActivityDetailBinding;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.utils.Network;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class DetailActivity extends AppCompatActivity {
    @Inject
    MovieViewModel movieViewModel;

    @Inject
    Executor executor;

    private final String bundleKey = "IS_FAVORITE";

    private ActivityDetailBinding binding;
    private Movie movie;
    private boolean originalFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        AndroidInjection.inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        binding.viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),
                Arrays.asList(getString(R.string.overview_tab_title),
                        getString(R.string.trailers_tab_title),
                        getString(R.string.reviews_tab_title)),
                Arrays.asList(new OverviewFragment(),
                        new TrailerFragment(),
                        new ReviewFragment())));

        binding.tabLayout.setupWithViewPager(binding.viewPager);

        Objects.requireNonNull(binding.tabLayout.getTabAt(0)).setIcon(R.drawable.ic_home);
        Objects.requireNonNull(binding.tabLayout.getTabAt(1)).setIcon(R.drawable.ic_movie);
        Objects.requireNonNull(binding.tabLayout.getTabAt(2)).setIcon(R.drawable.ic_thumb_up);

        movie = Objects.requireNonNull(getIntent().getExtras()).getParcelable(getString(R.string.intent_movie_key));

        if (savedInstanceState != null && savedInstanceState.containsKey(bundleKey)) {
            movie.isFavorite = savedInstanceState.getBoolean(bundleKey);
        }

        originalFavorite = Objects.requireNonNull(movie).isFavorite;
        binding.setMovie(movie);
        setFabImage(binding.fab);

        binding.fab.setOnClickListener(view -> {
            movie.isFavorite = !movie.isFavorite;
            setFabImage((FloatingActionButton) view);
            executor.execute(() -> movieViewModel.updateFavorite(movie.id, movie.isFavorite));
        });

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(movie.originalTitle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (originalFavorite != movie.isFavorite) {
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.intent_movie_id_key), movie.id);
                setResult(RESULT_OK, intent);
            }

            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(bundleKey, movie.isFavorite);
        super.onSaveInstanceState(outState);
    }

    @BindingAdapter("android:src")
    public static void setStringToImageView(ImageView imageView, String path) {
        Network.picasso(path).into(imageView);
    }

    private void setFabImage(FloatingActionButton fab) {
        fab.setImageResource(movie.isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
    }
}