package com.example.android.popularmovie;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovie.Utils.Converter;
import com.example.android.popularmovie.Utils.GsonWrapper;
import com.example.android.popularmovie.Utils.Network;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public final class MainFragment extends Fragment implements AsyncTaskCallback {

    @BindView(R.id.error_message_text_view)
    TextView errorMessageTextView;

    @BindView(R.id.grid_view)
    GridView gridView;

    @BindView(R.id.progress_bar)
    public ProgressBar progressBar;

    @BindString(R.string.parcelable_name)
    String parcelableName;

    @BindString(R.string.parcelable_arraylist_name)
    String parcelableArrayListName;

    @BindString(R.string.popular_movies_base_url)
    String popularMoviesBaseUrl;

    @BindString(R.string.top_rated_movies_base_url)
    String topRatedMoviesBaseUrl;

    String popularMoviesQueryUrl;
    String topRatedMoviesQueryUrl;

    private Activity activity;
    private ArrayList<Movie> movies;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (movies != null) {
            outState.putParcelableArrayList(parcelableArrayListName, movies);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        popularMoviesQueryUrl = Uri.parse(popularMoviesBaseUrl).buildUpon().appendQueryParameter("api_key", BuildConfig.API_KEY).toString();
        topRatedMoviesQueryUrl = Uri.parse(topRatedMoviesBaseUrl).buildUpon().appendQueryParameter("api_key", BuildConfig.API_KEY).toString();

        if (savedInstanceState != null && savedInstanceState.containsKey(parcelableArrayListName)) {
            movies = savedInstanceState.getParcelableArrayList(parcelableArrayListName);
        }

        activity = getActivity();

        if (movies != null) {
            gridView.setAdapter(new PosterAdapter(activity, movies));
        } else if (Network.isNetworkAvailable(activity)) {
            new DownloadAsyncTask(this).execute(popularMoviesQueryUrl);
        } else {
            showError("Network unavailable");
            return rootView;
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra(parcelableName, movies.get(i));
                startActivity(intent);
            }
        });

        setHasOptionsMenu(true);

        return rootView;
    }

    private void showError(String errorMessaage) {
        gridView.setVisibility(View.INVISIBLE);
        errorMessageTextView.setText(errorMessaage);
        errorMessageTextView.setVisibility(View.VISIBLE);
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
                new DownloadAsyncTask(this).execute(popularMoviesQueryUrl);
                return true;
            case R.id.order_by_highest_rated:
                new DownloadAsyncTask(this).execute(topRatedMoviesQueryUrl);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(String result) {
        progressBar.setVisibility(View.INVISIBLE);

        if (TextUtils.isEmpty(result)) {
            showError("Could not download movies");
            return;
        }

        movies = Converter.toArrayList(GsonWrapper.fromJson(result, "results", Movie[].class));
        gridView.setAdapter(new PosterAdapter(activity, movies));
    }
}