package com.example.android.popularmovie.ui.fragment;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovie.R;
import com.example.android.popularmovie.databinding.FragmentReviewBinding;
import com.example.android.popularmovie.databinding.ReviewViewHolderBinding;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.room.entity.Review;
import com.example.android.popularmovie.util.ApiResponse;
import com.example.android.popularmovie.util.ui.IntentUtils;
import com.example.android.popularmovie.viewmodel.MovieViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.example.android.popularmovie.ui.activity.DetailActivity.MOVIE_PARCELABLE_EXTRA_KEY;

public class ReviewFragment extends Fragment {
    @Inject
    MovieViewModel movieViewModel;

    private Context context;
    private FragmentReviewBinding binding;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ApiResponse<LiveData<List<Review>>> response = movieViewModel.getReviews(((Movie) IntentUtils.getParcelableExtra(this, MOVIE_PARCELABLE_EXTRA_KEY)).id);

        if (response.isSuccessful) {
            response.data.observe(this, reviews -> {
                if (!Objects.requireNonNull(reviews).isEmpty()) {
                    binding.recyclerView.setAdapter(new Adapter(reviews));
                }
            });
        }
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        private final List<Review> reviews;

        private Adapter(List<Review> reviews) {
            this.reviews = reviews;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(ReviewViewHolderBinding.inflate(LayoutInflater.from(context), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Review review = reviews.get(position);

            holder.binding.reviewLabelTextView.setText(context.getString(R.string.review_label_format, position + 1, review.author));
            holder.binding.reviewContentTextView.setText(review.content);
            holder.binding.reviewUrlTextView.setText(review.url);
        }

        @Override
        public int getItemCount() {
            return reviews.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final ReviewViewHolderBinding binding;

            ViewHolder(ReviewViewHolderBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}