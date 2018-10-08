package com.example.android.popularmovie;

import android.arch.lifecycle.LiveData;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popularmovie.adapter.MovieAdapter;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.databinding.ActivityMainBinding;
import com.example.android.popularmovie.utils.ApiResponse;
import com.example.android.popularmovie.utils.Network;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public final class MainActivity extends AppCompatActivity {
    private final String parcelableMoviesKey = "movies";
    private ActivityMainBinding binding;
    private ArrayList<Movie> movies;

    @Inject
    MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.grid_column_count)));

        if (savedInstanceState != null && savedInstanceState.containsKey(parcelableMoviesKey)) {
            movies = savedInstanceState.getParcelableArrayList(parcelableMoviesKey);
            binding.recyclerView.setAdapter(new MovieAdapter(movies));
        } else if (Network.isNetworkAvailable(this)) {
            populateMovies(movieViewModel.getPopularMovies());
        } else {
            showToast(getString(R.string.network_unavailable_error));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.order_by_most_popular:
                populateMovies(movieViewModel.getPopularMovies());
                break;
            case R.id.order_by_highest_rated:
                populateMovies(movieViewModel.getTopRatedMovies());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        // movies can be null if the network is unavailable, then the device rotates.
        if (movies != null) {
            outState.putParcelableArrayList(parcelableMoviesKey, movies);
        }
        super.onSaveInstanceState(outState);
    }

    private void populateMovies(ApiResponse<LiveData<List<Movie>>> response) {
        if (response.isSuccessful) {
            response.data.observe(this, movies -> {
                this.movies = (ArrayList<Movie>) movies;
                binding.recyclerView.setAdapter(new MovieAdapter(movies));
            });
        } else {
            showToast(response.errorMessage);
        }
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}