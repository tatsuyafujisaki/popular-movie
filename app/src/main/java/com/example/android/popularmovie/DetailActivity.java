package com.example.android.popularmovie;

import android.arch.lifecycle.LiveData;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovie.data.Movie;
import com.example.android.popularmovie.data.Review;
import com.example.android.popularmovie.databinding.ActivityDetailBinding;
import com.example.android.popularmovie.utils.ApiResponse;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class DetailActivity extends AppCompatActivity {
    private ArrayList<Review> reviews;

    @Inject
    DetailViewModel detailViewModel;

    @BindingAdapter("android:src")
    public static void setStringToImageView(ImageView imageView, String path) {
        Picasso.get().load(path).into(imageView);
    }

    @BindingAdapter("android:text")
    public static void setDoubleToTextView(TextView textView, Double d) {
        textView.setText(String.valueOf(d));
    }

    @BindingAdapter("android:text")
    public static void setLocalDateToTextView(TextView textView, LocalDate date) {
        textView.setText(date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);

        ActivityDetailBinding activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        String intentExtraKey = getString(R.string.intent_extra_key);
        Movie movie = Objects.requireNonNull(bundle).getParcelable(intentExtraKey);

        if (bundle.containsKey(intentExtraKey)) {
            activityDetailBinding.setMovie(movie);
        }

        ApiResponse<LiveData<List<Review>>> response = detailViewModel.getReviews(movie.id);

        if (response.isSuccessful) {
            response.data.observe(this, reviews -> {
                this.reviews = (ArrayList<Review>) reviews;

                if(!reviews.isEmpty()) {
                    activityDetailBinding.review.setText(reviews.get(0).content);
                }
            });
        }

        setSupportActionBar(activityDetailBinding.toolbar);
        activityDetailBinding.toolbar.setTitle(Objects.requireNonNull(movie).originalTitle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}