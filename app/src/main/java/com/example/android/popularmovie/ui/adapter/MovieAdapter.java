package com.example.android.popularmovie.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.android.popularmovie.R;
import com.example.android.popularmovie.databinding.MovieViewHolderBinding;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.ui.activity.DetailActivity;
import com.example.android.popularmovie.util.NetworkUtils;
import com.example.android.popularmovie.util.ui.IntentBuilder;

import java.util.List;

import static com.example.android.popularmovie.ui.activity.DetailActivity.MOVIE_PARCELABLE_EXTRA_KEY;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final Activity activity;
    private final List<Movie> movies;

    public MovieAdapter(final Activity activity, final List<Movie> movies) {
        this.activity = activity;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new ViewHolder(MovieViewHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        NetworkUtils.picasso(movies.get(position).posterPath).into(holder.binding.movieImageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final MovieViewHolderBinding binding;

        ViewHolder(final MovieViewHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            final Intent intent = new IntentBuilder(v.getContext(), DetailActivity.class)
                    .putParcelable(MOVIE_PARCELABLE_EXTRA_KEY, movies.get(getAdapterPosition()))
                    .build();

            activity.startActivityForResult(intent, v.getResources().getInteger(R.integer.activity_request_code));
        }
    }
}