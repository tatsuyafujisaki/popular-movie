package com.example.android.popularmovie.ui.activity;

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

import com.example.android.popularmovie.R;
import com.example.android.popularmovie.databinding.ActivityMainBinding;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.room.repository.MovieRepository.MovieType;
import com.example.android.popularmovie.ui.adapter.MovieAdapter;
import com.example.android.popularmovie.util.ApiResponse;
import com.example.android.popularmovie.util.NetworkUtils;
import com.example.android.popularmovie.util.IntentUtils;
import com.example.android.popularmovie.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public final class MainActivity extends AppCompatActivity {
    @Inject
    MovieViewModel movieViewModel;

    private final String bundleKey = "MOVIE_TYPE";
    private ActivityMainBinding binding;
    private ArrayList<Movie> movies;
    private MovieType movieType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, Math.max(2, getResources().getDisplayMetrics().widthPixels / getResources().getInteger(R.integer.poster_grid_column_width))));

        if (!NetworkUtils.isNetworkAvailable(this)) {
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
        if (requestCode == getResources().getInteger(R.integer.activity_request_code) && resultCode == RESULT_OK) {
            int movieId = IntentUtils.getIntExtra(data);
            if (movieType == MovieType.FAVORITE) {
                for (Movie movie : movies) {
                    if (movie.id == movieId) {
                        movies.remove(movie);
                    }
                }
                binding.recyclerView.setAdapter(new MovieAdapter(this, movies));
            } else {
                for (Movie movie : movies) {
                    if(movie.id == movieId){
                        movie.isFavorite = ! movie.isFavorite;
                        break;
                    }
                }
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
                /*
                 * This observer is called twice.
                 * For the first time, movies is null because downloading movies has not completed.
                 * For the second time, movies is not null because downloading movies in a different thread has completed.
                 */
                if (!Objects.requireNonNull(movies).isEmpty()) {
                    this.movies = (ArrayList<Movie>) movies;
                    binding.recyclerView.setAdapter(new MovieAdapter(this, movies));
                    response.data.removeObservers(this);
                }
            });
        } else {
            showToast(response.errorMessage);
        }
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}