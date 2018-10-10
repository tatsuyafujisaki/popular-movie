package com.example.android.popularmovie;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popularmovie.adapter.MovieAdapter;
import com.example.android.popularmovie.databinding.ActivityMainBinding;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.room.repository.MovieRepository.MovieType;
import com.example.android.popularmovie.utils.ApiResponse;
import com.example.android.popularmovie.utils.Network;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public final class MainActivity extends AppCompatActivity {
    private final String bundleKey = "MOVIE_TYPE";
    private ActivityMainBinding binding;
    private ArrayList<Movie> movies;
    private MovieType movieType;

    @Inject
    MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.grid_column_count)));

        if (!Network.isNetworkAvailable(this)) {
            showToast(getString(R.string.network_unavailable_error));
            return;
        }

        movieType = savedInstanceState != null && savedInstanceState.containsKey(bundleKey) ? (MovieType) savedInstanceState.get(bundleKey) : MovieType.POPULAR;

        setMovies(movieViewModel.getMovies(movieType));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                movieType = MovieType.POPULAR;
                break;
            case R.id.top_rated:
                movieType = MovieType.TOP_RATED;
                break;
            case R.id.favorites:
                movieType = MovieType.FAVORITE;
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(item.getItemId()));
        }

        setMovies(movieViewModel.getMovies(movieType));

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // movies is null if the device rotates in DetailActivity.
        if (movies == null) {
            return;
        }

        // If movies is not null and the favorite flag of a movie is toggled in DetailActivity, update the favorite flag in movies too.
        if (requestCode == getResources().getInteger(R.integer.get_movie_id_if_favorite_toggled) && resultCode == RESULT_OK) {
            int movieId = data.getIntExtra(getString(R.string.intent_movie_id_key), -1);
            if (movieType == MovieType.FAVORITE) {
                movies.removeIf(movie -> movie.id == movieId);
                binding.recyclerView.setAdapter(new MovieAdapter(this, movies));
            } else {
                movies.stream().filter(movie -> movie.id == movieId).findFirst().ifPresent(movie -> movie.isFavorite = !movie.isFavorite);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(bundleKey, movieType);
        super.onSaveInstanceState(outState);
    }

    private void setMovies(ApiResponse<LiveData<List<Movie>>> response) {
        if (response.isSuccessful) {
            response.data.observe(this, movies -> {
                this.movies = (ArrayList<Movie>) movies;
                binding.recyclerView.setAdapter(new MovieAdapter(this, movies));
                response.data.removeObservers(this);
            });
        } else {
            showToast(response.errorMessage);
        }
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}