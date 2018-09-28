package com.example.android.popularmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.example.android.popularmovie.utils.Converter;
import com.example.android.popularmovie.utils.GsonWrapper;
import com.example.android.popularmovie.utils.Network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class MainFragment extends Fragment implements MovieAdapter.ClickListener {
    private final String parcelableArrayListKey = getClass().getSimpleName();
    private ArrayList<Movie> movies;
    private FragmentMainBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.grid_column_count)));
        binding.recyclerView.setHasFixedSize(true);

        setHasOptionsMenu(true);

        if (savedInstanceState != null && savedInstanceState.containsKey(parcelableArrayListKey)) {
            movies = savedInstanceState.getParcelableArrayList(parcelableArrayListKey);
        }

        if (movies != null) {
            binding.recyclerView.setAdapter(new MovieAdapter(movies, this));
        } else if (Network.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            TmdbClient.init(getString(R.string.tmdb_base_url),
                    new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    movies = Converter.toArrayList(GsonWrapper.fromJson(GsonWrapper.getJsonArray(Objects.requireNonNull(response.body()).string(), getString(R.string.tmdb_json_results_element)), Movie[].class));
                                } catch (IOException e) {
                                    showToast(e.getMessage());
                                }

                                for (Movie movie : movies) {
                                    movie.posterPath = getString(R.string.poster_base_url).concat(movie.posterPath);
                                }

                                binding.recyclerView.setAdapter(new MovieAdapter(movies, MainFragment.this));
                            } else {
                                try {
                                    showToast(Objects.requireNonNull(response.errorBody()).string());
                                } catch (IOException e) {
                                    showToast(e.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            showToast(t.getMessage());
                        }
                    });
            TmdbClient.DownloadPopularMovies();
        } else {
            showToast(getString(R.string.network_unavailable_error));
        }

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (movies != null) {
            outState.putParcelableArrayList(parcelableArrayListKey, movies);
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
                TmdbClient.DownloadPopularMovies();
                return true;
            case R.id.order_by_highest_rated:
                TmdbClient.DownloadTopRatedMovies();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int index) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(getString(R.string.intent_extra_key), movies.get(index));
        startActivity(intent);
    }

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }
}