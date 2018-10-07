package com.example.android.popularmovie;

import android.databinding.BindingAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovie.data.Movie;
import com.example.android.popularmovie.databinding.FragmentDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class DetailFragment extends Fragment {
    private final String parcelableMovieKey = "movie";

    @Inject
    DetailViewModel detailViewModel;

    private FragmentDetailBinding binding;

    private Movie movie;

    @BindingAdapter("android:src")
    public static void setStringToImageView(ImageView imageView, String path) {
        Picasso.get().load(path).into(imageView);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);

        binding.viewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),
                Arrays.asList("Info", "Trailers", "Reviews"),
                Arrays.asList(new TabFragment(), new TabFragment(), new TabFragment())));

        binding.tabLayout.setupWithViewPager(binding.viewPager);

        Objects.requireNonNull(binding.tabLayout.getTabAt(0)).setIcon(R.drawable.ic_home);
        Objects.requireNonNull(binding.tabLayout.getTabAt(1)).setIcon(R.drawable.ic_movie);
        Objects.requireNonNull(binding.tabLayout.getTabAt(2)).setIcon(R.drawable.ic_thumb_up);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);

        setMovie(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) Objects.requireNonNull(getActivity());
        activity.setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(movie.originalTitle);
        Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(parcelableMovieKey, movie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Objects.requireNonNull(getActivity()).onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMovie(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(parcelableMovieKey)) {
            movie = savedInstanceState.getParcelable(parcelableMovieKey);
        } else {
            Bundle bundle = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras());
            String intentExtraKey = getString(R.string.intent_extra_key);

            if (!bundle.containsKey(intentExtraKey)) {
                throw new IllegalStateException();
            }

            movie = Objects.requireNonNull(bundle).getParcelable(intentExtraKey);
        }

        binding.setMovie(movie);
    }
}