package com.example.android.popularmovie.ui.fragment;

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

import com.example.android.popularmovie.viewmodel.MovieViewModel;
import com.example.android.popularmovie.R;
import com.example.android.popularmovie.ui.adapter.TrailerAdapter;
import com.example.android.popularmovie.databinding.FragmentTrailerBinding;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.room.entity.Trailer;
import com.example.android.popularmovie.util.ApiResponse;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class TrailerFragment extends Fragment {
    @Inject
    MovieViewModel movieViewModel;

    private FragmentTrailerBinding binding;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTrailerBinding.inflate(inflater, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Movie movie = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getParcelable(getString(R.string.intent_movie_key));
        ApiResponse<LiveData<List<Trailer>>> response = movieViewModel.getTrailers(Objects.requireNonNull(movie).id);

        if (response.isSuccessful) {
            response.data.observe(this, trailers -> {
                if (!Objects.requireNonNull(trailers).isEmpty()) {
                    binding.recyclerView.setAdapter(new TrailerAdapter(trailers));
                }
            });
        }
    }
}