package com.example.android.popularmovie.ui.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovie.R;
import com.example.android.popularmovie.databinding.*;
import com.example.android.popularmovie.room.entity.Trailer;

import java.util.List;

public class TrailerRecyclerViewAdapter extends RecyclerView.Adapter<TrailerRecyclerViewAdapter.ViewHolder> {
    private final List<Trailer> trailers;
    private Context context;

    public TrailerRecyclerViewAdapter(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(TrailerRecyclerViewItemBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);

        holder.binding.trailerLabelTextView.setText(trailer.name);
        holder.binding.trailerInfoTextView.setText(context.getString(R.string.trailer_info_format, trailer.iso639, trailer.iso3166, trailer.size));
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TrailerRecyclerViewItemBinding binding;

        ViewHolder(TrailerRecyclerViewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Trailer trailer = trailers.get(getAdapterPosition());

            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.youtube_base_uri).concat(trailer.key))));
            } catch (ActivityNotFoundException e) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.youtube_base_url).concat(trailer.key))));
            }
        }
    }
}