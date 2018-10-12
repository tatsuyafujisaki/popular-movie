package com.example.android.popularmovie;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovie.adapter.ReviewAdapter;
import com.example.android.popularmovie.databinding.FragmentReviewBinding;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.room.entity.Review;
import com.example.android.popularmovie.utils.ApiResponse;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class ReviewFragment extends Fragment {
    @Inject
    MovieViewModel movieViewModel;

    private FragmentReviewBinding binding;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Movie movie = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getParcelable(getString(R.string.intent_movie_key));
        ApiResponse<LiveData<List<Review>>> response = movieViewModel.getReviews(Objects.requireNonNull(movie).id);

        if (response.isSuccessful) {
            response.data.observe(this, reviews -> {
                if (!Objects.requireNonNull(reviews).isEmpty()) {
                    binding.recyclerView.setAdapter(new ReviewAdapter(reviews));
                }
            });
        }
    }
}