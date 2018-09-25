package com.example.android.popularmovie;

import android.content.Context;
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

import com.example.android.popularmovie.Utils.Converter;
import com.example.android.popularmovie.Utils.GsonWrapper;
import com.example.android.popularmovie.Utils.Network;
import com.example.android.popularmovie.databinding.FragmentMainBinding;

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

        Context context = Objects.requireNonNull(getContext());
        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, getResources().getInteger(R.integer.grid_column_count)));
        binding.recyclerView.setHasFixedSize(true);

        setHasOptionsMenu(true);

        if (savedInstanceState != null && savedInstanceState.containsKey(parcelableArrayListKey)) {
            movies = savedInstanceState.getParcelableArrayList(parcelableArrayListKey);
        }

        super.onActivityCreated(savedInstanceState);
        if (movies != null) {
            binding.recyclerView.setAdapter(new MovieAdapter(movies, this));
        } else if (Network.isNetworkAvailable(context)) {
            TmdbServiceWrapper.init(getString(R.string.tmdb_base_url), new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            movies = Converter.toArrayList(GsonWrapper.fromJson(GsonWrapper.getJsonArray(Objects.requireNonNull(response.body()).string(), getString(R.string.tmdb_json_results_element)), Movie[].class));
                        } catch (IOException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        for (Movie movie : movies) {
                            movie.posterPath = getString(R.string.poster_base_url).concat(movie.posterPath);
                        }

                        binding.recyclerView.setAdapter(new MovieAdapter(movies, MainFragment.this));
                    } else {
                        try {
                            Toast.makeText(context, Objects.requireNonNull(response.errorBody()).string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            TmdbServiceWrapper.DownloadPopularMovies();
        } else {
            Toast.makeText(context, getString(R.string.network_unavailable_error), Toast.LENGTH_LONG).show();
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
                TmdbServiceWrapper.DownloadPopularMovies();
                return true;
            case R.id.order_by_highest_rated:
                TmdbServiceWrapper.DownloadTopRatedMovies();
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
}