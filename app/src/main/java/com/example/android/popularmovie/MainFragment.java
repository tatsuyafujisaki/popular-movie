package com.example.android.popularmovie;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.popularmovie.data.Movie;
import com.example.android.popularmovie.databinding.FragmentMainBinding;
import com.example.android.popularmovie.utils.ApiResponse;
import com.example.android.popularmovie.utils.Network;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public final class MainFragment extends Fragment implements MovieAdapter.ClickListener {
    private final String parcelableMoviesKey = "movies";
    private FragmentMainBinding binding;
    private ArrayList<Movie> movies;

    @Inject
    MainViewModel mainViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.grid_column_count)));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(parcelableMoviesKey)) {
            movies = savedInstanceState.getParcelableArrayList(parcelableMoviesKey);
            binding.recyclerView.setAdapter(new MovieAdapter(movies, this));
            setHasOptionsMenu(true);
        } else if (Network.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            populateMovies(mainViewModel.getPopularMovies());
            setHasOptionsMenu(true);
        } else {
            showToast(getString(R.string.network_unavailable_error));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        // movies can be null when the app starts when the network is unavailable, then the device rotates.
        if (movies != null) {
            outState.putParcelableArrayList(parcelableMoviesKey, movies);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.order_by_most_popular:
                populateMovies(mainViewModel.getPopularMovies());
                break;
            case R.id.order_by_highest_rated:
                populateMovies(mainViewModel.getTopRatedMovies());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int index) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(getString(R.string.intent_extra_key), movies.get(index));
        startActivity(intent);
    }

    private void populateMovies(ApiResponse<LiveData<List<Movie>>> response) {
        if (response.isSuccessful) {
            response.data.observe(this, movies -> {
                this.movies = (ArrayList<Movie>) movies;
                binding.recyclerView.setAdapter(new MovieAdapter(movies, this));
            });
        } else {
            showToast(response.errorMessage);
        }
    }

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }
}