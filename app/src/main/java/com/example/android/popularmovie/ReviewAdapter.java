package com.example.android.popularmovie;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.popularmovie.data.Review;
import com.example.android.popularmovie.databinding.ReviewRecyclerviewItemBinding;

import java.util.List;

final class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private final List<Review> reviews;

    ReviewAdapter(List<Review> reviews) {
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
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ReviewRecyclerviewItemBinding binding;

        ViewHolder(ReviewRecyclerviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}