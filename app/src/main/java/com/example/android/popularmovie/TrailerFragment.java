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

import com.example.android.popularmovie.adapter.TrailerAdapter;
import com.example.android.popularmovie.databinding.FragmentTrailerBinding;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.room.entity.Trailer;
import com.example.android.popularmovie.utils.ApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class TrailerFragment extends Fragment {
    private final String parcelableMovieKey = "movie";
    private final String parcelableTrailersKey = "trailers";

    @Inject
    MovieViewModel movieViewModel;

    private FragmentTrailerBinding binding;

    private Movie movie;
    private ArrayList<Trailer> trailers;

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

        setMovie(savedInstanceState);
        setTrailers(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(parcelableMovieKey, movie);

        if (trailers != null) {
            outState.putParcelableArrayList(parcelableTrailersKey, trailers);
        }

        super.onSaveInstanceState(outState);
    }

    private void setMovie(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(parcelableMovieKey)) {
            movie = savedInstanceState.getParcelable(parcelableMovieKey);
        } else {
            Bundle bundle = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras());

            String extraKey = getString(R.string.intent_movie_key);

            if (!bundle.containsKey(extraKey)) {
                throw new IllegalStateException();
            }

            movie = bundle.getParcelable(extraKey);
        }
    }

    private void setTrailers(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(parcelableTrailersKey)) {
            trailers = savedInstanceState.getParcelableArrayList(parcelableTrailersKey);
            binding.recyclerView.setAdapter(new TrailerAdapter(trailers));
        } else {
            ApiResponse<LiveData<List<Trailer>>> response = movieViewModel.getTrailers(movie.id);

            if (response.isSuccessful) {
                response.data.observe(this, trailers -> {
                    if (!Objects.requireNonNull(trailers).isEmpty()) {
                        this.trailers = (ArrayList<Trailer>) trailers;
                        binding.recyclerView.setAdapter(new TrailerAdapter(trailers));
                    }
                });
            }
        }
    }
}