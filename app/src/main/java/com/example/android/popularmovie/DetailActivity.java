package com.example.android.popularmovie;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.android.popularmovie.adapter.MyFragmentPagerAdapter;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class DetailActivity extends AppCompatActivity {
    @Inject
    MovieViewModel movieViewModel;

    private final String parcelableMovieKey = "movie";

    private ActivityDetailBinding binding;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        AndroidInjection.inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        binding.viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),
                Arrays.asList(getString(R.string.info_tab_title),
                              getString(R.string.trailers_tab_title),
                              getString(R.string.reviews_tab_title)),
                Arrays.asList(new OverviewFragment(),
                              new TrailerFragment(),
                              new ReviewFragment())));

        binding.tabLayout.setupWithViewPager(binding.viewPager);

        Objects.requireNonNull(binding.tabLayout.getTabAt(0)).setIcon(R.drawable.ic_home);
        Objects.requireNonNull(binding.tabLayout.getTabAt(1)).setIcon(R.drawable.ic_movie);
        Objects.requireNonNull(binding.tabLayout.getTabAt(2)).setIcon(R.drawable.ic_thumb_up);

        setMovie(savedInstanceState);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(movie.originalTitle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(parcelableMovieKey, movie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @BindingAdapter("android:src")
    public static void setStringToImageView(ImageView imageView, String path) {
        Picasso.get().load(path).into(imageView);
    }

    private void setMovie(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(parcelableMovieKey)) {
            movie = savedInstanceState.getParcelable(parcelableMovieKey);
        } else {
            Bundle bundle = Objects.requireNonNull(getIntent().getExtras());
            String intentExtraKey = getString(R.string.intent_extra_key);

            if (!bundle.containsKey(intentExtraKey)) {
                throw new IllegalStateException();
            }

            movie = Objects.requireNonNull(bundle).getParcelable(intentExtraKey);
        }

        binding.setMovie(movie);
    }
}