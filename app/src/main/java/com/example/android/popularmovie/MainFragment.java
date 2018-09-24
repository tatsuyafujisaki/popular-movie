package com.example.android.popularmovie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
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

import java.util.ArrayList;

public final class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>, MovieAdapter.ClickListener {
    private static final int LOADER_ID = 0;

    private final String parcelableArrayListKey = getClass().getSimpleName();
    private final String bundleExtraKey = getClass().getSimpleName();
    private FragmentMainBinding binding;
    private ArrayList<Movie> movies;
    private String popularMoviesQueryUrl;
    private String topRatedMoviesQueryUrl;

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

        popularMoviesQueryUrl = Uri.parse(getString(R.string.popular_movies_base_url))
                .buildUpon()
                .appendQueryParameter("api_key", BuildConfig.API_KEY)
                .toString();

        topRatedMoviesQueryUrl = Uri.parse(getString(R.string.top_rated_movies_base_url))
                .buildUpon()
                .appendQueryParameter("api_key", BuildConfig.API_KEY)
                .toString();

        if (savedInstanceState != null && savedInstanceState.containsKey(parcelableArrayListKey)) {
            movies = savedInstanceState.getParcelableArrayList(parcelableArrayListKey);
        }

        if (movies != null) {
            binding.recyclerView.setAdapter(new MovieAdapter(getContext(), movies, this));
        } else if (Network.isNetworkAvailable(getContext())) {
            DownloadMovies(popularMoviesQueryUrl);
        } else {
            showError("Network unavailable");
            return rootView;
        }

        setHasOptionsMenu(true);

        return rootView;
    }

    private void DownloadMovies(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(bundleExtraKey, url);

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        if (loaderManager.getLoader(LOADER_ID) == null) {
            loaderManager.initLoader(LOADER_ID, bundle, this);
        } else {
            loaderManager.restartLoader(LOADER_ID, bundle, this);
        }
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
                DownloadMovies(popularMoviesQueryUrl);
                return true;
            case R.id.order_by_highest_rated:
                DownloadMovies(topRatedMoviesQueryUrl);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        binding.progressBar.setVisibility(View.VISIBLE);
        return new MyAsyncTaskLoader(getContextNonNull(), args.getString(bundleExtraKey));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        binding.progressBar.setVisibility(View.INVISIBLE);

        if (TextUtils.isEmpty(data)) {
            showError("Could not download movies");
            return;
        }

        movies = Converter.toArrayList(GsonWrapper.fromJson(data, "results", Movie[].class));
        binding.recyclerView.setAdapter(new MovieAdapter(getContextNonNull(), movies, this));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
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

    private Activity getActivityNonNull() {
        Activity activity = getActivity();

        if (activity == null) {
            throw new RuntimeException("getActivity() returns nulll");
        }

        return activity;
    }
}