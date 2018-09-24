package com.example.android.popularmovie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

final class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final String posterBaseUrl;
    private final List<Movie> movies;
    private final ClickListener clickListener;

    MovieAdapter(Context context, List<Movie> movies, ClickListener clickListener) {
        posterBaseUrl = context.getString(R.string.poster_base_url);
        this.movies = movies;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.get().load(posterBaseUrl + movies.get(position).posterPath).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    interface ClickListener {
        void onClick(int index);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final ImageView imageView;

        ViewHolder(View view) {
            super(view);
            imageView = itemView.findViewById(R.id.poster_thumbnail_view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(getAdapterPosition());
        }
    }
}
