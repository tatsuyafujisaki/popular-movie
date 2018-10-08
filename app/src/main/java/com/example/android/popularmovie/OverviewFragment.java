package com.example.android.popularmovie;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.databinding.FragmentOverviewBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import dagger.android.support.AndroidSupportInjection;

public class OverviewFragment extends Fragment {
    private final String parcelableMovieKey = "movie";

    private FragmentOverviewBinding binding;

    private Movie movie;

    @BindingAdapter("android:text")
    public static void setDoubleToTextView(TextView textView, Double d) {
        textView.setText(String.valueOf(d));
    }

    @BindingAdapter("android:text")
    public static void setLocalDateToTextView(TextView textView, LocalDate date) {
        textView.setText(date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMovie(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(parcelableMovieKey, movie);
        super.onSaveInstanceState(outState);
    }

    private void setMovie(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(parcelableMovieKey)) {
            movie = savedInstanceState.getParcelable(parcelableMovieKey);
        } else {
            Bundle bundle = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras());

            String extraKey = getString(R.string.intent_extra_key);

            if (!bundle.containsKey(extraKey)) {
                throw new IllegalStateException();
            }

            movie = bundle.getParcelable(extraKey);
        }

        binding.setMovie(movie);
    }
}