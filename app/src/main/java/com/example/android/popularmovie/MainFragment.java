package com.example.android.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovie.Utils.Converter;
import com.example.android.popularmovie.Utils.GsonWrapper;
import com.example.android.popularmovie.Utils.Network;
import com.example.android.popularmovie.databinding.FragmentMainBinding;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public final class MainFragment extends Fragment implements MovieAdapter.ClickListener {
    private final String parcelableArrayListKey = getClass().getSimpleName();
    private final String bundleExtraKey = getClass().getSimpleName();
    private FragmentMainBinding binding;
    private ArrayList<Movie> movies;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (movies != null) {
            outState.putParcelableArrayList(parcelableArrayListKey, movies);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        binding = DataBindingUtil.setContentView(getActivityNonNull(), R.layout.fragment_main);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), getContextNonNull().getResources().getInteger(R.integer.grid_column_count)));

        if (savedInstanceState != null && savedInstanceState.containsKey(parcelableArrayListKey)) {
            movies = savedInstanceState.getParcelableArrayList(parcelableArrayListKey);
        }

        if (movies != null) {
            binding.recyclerView.setAdapter(new MovieAdapter(movies, this));
        } else if (Network.isNetworkAvailable(getContextNonNull())) {
            DownloadMovies();
        } else {
            showError("Network unavailable");
            return rootView;
        }

        setHasOptionsMenu(true);

        return rootView;
    }

    private void DownloadMovies() {
        new Retrofit.Builder()
                .baseUrl(getString(R.string.tmdb_base_url))
                .build()
                .create(TmdbService.class)
                .getPopularMovies(BuildConfig.API_KEY)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            ResponseBody rb = response.body();

                            if (rb == null) {
                                return;
                            }
                            
                            try {
                                movies = Converter.toArrayList(GsonWrapper.fromJson(GsonWrapper.getJsonArray(rb.string(), "results"), Movie[].class));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            for (Movie movie : movies) {
                                movie.posterPath = getString(R.string.poster_base_url).concat(movie.posterPath);
                            }

                            binding.recyclerView.setAdapter(new MovieAdapter(movies, MainFragment.this));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(getClass().getSimpleName(), t.getMessage());
                    }
                });
    }

    private void showError(String errorMessaage) {
        binding.recyclerView.setVisibility(View.INVISIBLE);
        binding.errorMessageTextView.setText(errorMessaage);
        binding.errorMessageTextView.setVisibility(View.VISIBLE);
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
                DownloadMovies();
                return true;
            case R.id.order_by_highest_rated:
                DownloadMovies();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int index) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(getString(R.string.parcelable_key), movies.get(index));
        startActivity(intent);
    }

    private Context getContextNonNull() {
        Context context = getContext();

        if (context == null) {
            throw new RuntimeException("getContext() returns nulll");
        }

        return context;
    }

    private FragmentActivity getActivityNonNull() {
        FragmentActivity activity = getActivity();

        if (activity == null) {
            throw new RuntimeException("getActivity() returns nulll");
        }

        return activity;
    }
}