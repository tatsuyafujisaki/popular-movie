package com.example.android.popularmovie;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public final class MainFragment extends Fragment {

    @BindView(R.id.error_message_text_view)
    TextView errorMessageTextView;

    @BindView(R.id.grid_view)
    GridView gridView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindString(R.string.parcelable_name)
    String parcelableName;

    @BindString(R.string.parcelable_arraylist_name)
    String parcelableArrayListName;

    @BindString(R.string.popular_movies_base_url)
    String popularMoviesBaseUrl;

    @BindString(R.string.top_rated_movies_base_url)
    String topRatedMoviesBaseUrl;

    @BindString(R.string.api_key)
    String apiKey;

    String popularMoviesQueryUrl;
    String topRatedMoviesQueryUrl;

    private ArrayList<Movie> movies;

    public MainFragment() {
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(parcelableArrayListName, movies);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        if (!Network.isNetworkAvailable(getActivity())) {
            showError("Network unavailable");
            return rootView;
        }

        popularMoviesQueryUrl = Uri.parse(popularMoviesBaseUrl).buildUpon().appendQueryParameter("api_key", apiKey).toString();
        topRatedMoviesQueryUrl = Uri.parse(topRatedMoviesBaseUrl).buildUpon().appendQueryParameter("api_key", apiKey).toString();

        if (savedInstanceState != null && savedInstanceState.containsKey(parcelableArrayListName)) {
            setMovies(savedInstanceState.getParcelableArrayList(parcelableArrayListName));
        } else {
            new MovieDownloader().execute(popularMoviesQueryUrl);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(parcelableName, movies.get(i));
                startActivity(intent);
            }
        });

        setHasOptionsMenu(true);

        return rootView;
    }

    private void setMovies(ArrayList<? extends Parcelable> parcelables) {
        movies = (ArrayList<Movie>) parcelables;
        gridView.setAdapter(new PosterAdapter(getActivity(), movies));
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
                new MovieDownloader().execute(popularMoviesQueryUrl);
                return true;
            case R.id.order_by_highest_rated:
                new MovieDownloader().execute(topRatedMoviesQueryUrl);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    final class MovieDownloader extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return Network.getResponse(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            progressBar.setVisibility(View.INVISIBLE);
            if (TextUtils.isEmpty(json)) {
                showError("Invalid JSON");
                return;
            }

            setMovies(Converter.toArrayList(GsonWrapper.fromJson(json, "results", Movie[].class)));
        }
    }
}