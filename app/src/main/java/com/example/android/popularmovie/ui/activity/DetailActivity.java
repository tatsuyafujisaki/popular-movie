package com.example.android.popularmovie.ui.activity;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.android.popularmovie.R;
import com.example.android.popularmovie.databinding.ActivityDetailBinding;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.ui.adapter.MyFragmentPagerAdapter;
import com.example.android.popularmovie.ui.fragment.OverviewFragment;
import com.example.android.popularmovie.ui.fragment.ReviewFragment;
import com.example.android.popularmovie.ui.fragment.TrailerFragment;
import com.example.android.popularmovie.util.NetworkUtils;
import com.example.android.popularmovie.util.ui.IntentUtils;
import com.example.android.popularmovie.viewmodel.MovieViewModel;

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
        AndroidInjection.inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        binding.viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),
                Arrays.asList(getString(R.string.overview_tab),
                        getString(R.string.trailers_tab),
                        getString(R.string.reviews_tabs)),
                Arrays.asList(new OverviewFragment(),
                        new TrailerFragment(),
                        new ReviewFragment())));

        binding.tabLayout.setupWithViewPager(binding.viewPager);

        Objects.requireNonNull(binding.tabLayout.getTabAt(0)).setIcon(R.drawable.ic_home);
        Objects.requireNonNull(binding.tabLayout.getTabAt(1)).setIcon(R.drawable.ic_movie);
        Objects.requireNonNull(binding.tabLayout.getTabAt(2)).setIcon(R.drawable.ic_thumb_up);

        movie = IntentUtils.getParcelableExtra(this);

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
                setResult(RESULT_OK, IntentUtils.createIntent(movie.id));
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
        NetworkUtils.picasso(path).into(imageView);
    }

    private void setFabImage(FloatingActionButton fab) {
        fab.setImageResource(movie.isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
    }
}