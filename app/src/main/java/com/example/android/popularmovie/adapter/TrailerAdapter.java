package com.example.android.popularmovie.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.popularmovie.R;
import com.example.android.popularmovie.room.entity.Trailer;
import com.example.android.popularmovie.databinding.TrailerRecyclerviewItemBinding;

import java.util.List;

public final class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private final List<Trailer> trailers;
    private Context context;

    public TrailerAdapter(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(TrailerRecyclerviewItemBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);

        holder.binding.trailerLabelTextView.setText(trailer.name);
        holder.binding.trailerInfoTextView.setText(context.getString(R.string.trailer_info, trailer.iso639, trailer.iso3166, trailer.size));
        holder.binding.trailerUrlTextView.setText(context.getString(R.string.youtube_base_url).concat(trailer.key));
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TrailerRecyclerviewItemBinding binding;

        ViewHolder(TrailerRecyclerviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}