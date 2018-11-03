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
import com.example.android.popularmovie.databinding.MovieRecyclerViewItemBinding;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.ui.activity.DetailActivity;
import com.example.android.popularmovie.util.NetworkUtils;
import com.example.android.popularmovie.util.ui.IntentBuilder;
import com.example.android.popularmovie.util.ui.ResourceUtils;

import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {
    private final Activity activity;
    private final List<Movie> movies;

    public MovieRecyclerViewAdapter(Activity activity, List<Movie> movies) {
        this.activity = activity;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(MovieRecyclerViewItemBinding.inflate(LayoutInflater.from(activity), parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NetworkUtils.picasso(movies.get(position).posterPath).into(holder.binding.movieImageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final MovieRecyclerViewItemBinding binding;

        ViewHolder(MovieRecyclerViewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new IntentBuilder(activity, DetailActivity.class).putParcelable(null, movies.get(getAdapterPosition())).build();
            activity.startActivityForResult(intent, ResourceUtils.getInteger(activity, R.integer.activity_request_code));
        }
    }
}