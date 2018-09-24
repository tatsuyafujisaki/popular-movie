package com.example.android.popularmovie;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.popularmovie.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();

        String parcelableKey = getString(R.string.parcelable_key);

        if (bundle != null && bundle.containsKey(parcelableKey)) {
            Movie movie = bundle.getParcelable(parcelableKey);

            if (movie != null) {
                Picasso.get().load(getString(R.string.poster_base_url) + movie.posterPath).into(binding.posterImageView);

                binding.originalTitleTextView.setText(movie.originalTitle);
                binding.releaseDateTextView.setText(new SimpleDateFormat("MMMM d, yyyy", Locale.US).format(movie.releaseDate));
                binding.userRatingTextView.setText(String.valueOf(movie.userRating));
                binding.plotSynopsisTextView.setText(movie.plotSynopsis);
            }
        }
    }
}