package com.example.android.popularmovie.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.android.popularmovie.DetailActivity;
import com.example.android.popularmovie.R;
import com.example.android.popularmovie.databinding.MovieRecyclerviewItemBinding;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.utils.Network;

import java.util.List;

public final class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final Activity activity;
    private final List<Movie> movies;

    public MovieAdapter(Activity activity, List<Movie> movies) {
        this.activity = activity;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(MovieRecyclerviewItemBinding.inflate(LayoutInflater.from(activity), parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Network.picasso(movies.get(position).posterPath).into(holder.binding.movieImageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final MovieRecyclerviewItemBinding binding;

        ViewHolder(MovieRecyclerviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, DetailActivity.class);
            intent.putExtra(activity.getString(R.string.intent_movie_key), movies.get(getAdapterPosition()));
            activity.startActivityForResult(intent, activity.getResources().getInteger(R.integer.activity_request_code));
        }
    }
}