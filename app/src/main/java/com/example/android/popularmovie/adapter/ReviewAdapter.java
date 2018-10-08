package com.example.android.popularmovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.android.popularmovie.R;
import com.example.android.popularmovie.data.Review;
import com.example.android.popularmovie.databinding.ReviewRecyclerviewItemBinding;

import java.util.List;

public final class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private final Context context;
    private final List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ReviewRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.binding.reviewLabelTextView.setText(holder.itemView.getResources().getString(R.string.review_label, position + 1, review.author));
        holder.binding.reviewContentTextView.setText(review.content);
        holder.binding.reviewUrlTextView.setText(review.url);
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(review.url)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final ReviewRecyclerviewItemBinding binding;

        ViewHolder(ReviewRecyclerviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void onClick(View v) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(reviews.get(getAdapterPosition()).url)));
        }
    }
}