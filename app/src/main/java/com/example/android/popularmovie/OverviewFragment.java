package com.example.android.popularmovie;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovie.data.Movie;
import com.example.android.popularmovie.data.Review;
import com.example.android.popularmovie.databinding.FragmentOverviewBinding;
import com.example.android.popularmovie.utils.ApiResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class OverviewFragment extends Fragment {
    private final String parcelableMovieKey = "movie";
    private final String parcelableReviewsKey = "reviews";

    @Inject
    DetailViewModel detailViewModel;

    private FragmentOverviewBinding binding;

    private Movie movie;
    private ArrayList<Review> reviews;

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
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setMovie(savedInstanceState);
        setReviews(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(parcelableMovieKey, movie);

        if (reviews != null) {
            outState.putParcelableArrayList(parcelableReviewsKey, reviews);
        }

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

    private void setReviews(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(parcelableReviewsKey)) {
            reviews = savedInstanceState.getParcelableArrayList(parcelableReviewsKey);
            binding.recyclerView.setAdapter(new ReviewAdapter(reviews));
        } else {
            ApiResponse<LiveData<List<Review>>> response = detailViewModel.getReviews(movie.id);

            if (response.isSuccessful) {
                response.data.observe(this, reviews -> {
                    if (!Objects.requireNonNull(reviews).isEmpty()) {
                        this.reviews = (ArrayList<Review>) reviews;
                        binding.recyclerView.setAdapter(new ReviewAdapter(reviews));
                    }
                });
            }
        }
    }
}