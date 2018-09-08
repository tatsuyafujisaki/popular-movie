package com.example.android.popularmovie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.original_title_text_view)
    TextView originalTitleTextView;

    @BindView(R.id.poster_image_view)
    ImageView posterImageView;

    @BindView(R.id.plot_synopsis_text_view)
    TextView plotSynopsisTitleTextView;

    @BindView(R.id.user_rating_text_view)
    TextView userRatingTextView;

    @BindView(R.id.release_date_text_view)
    TextView releaseDateTextView;

    @BindString(R.string.parcelable_name)
    String parcelableName;

    @BindString(R.string.poster_base_url)
    String posterBaseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey(parcelableName)) {
            Movie movie = bundle.getParcelable(parcelableName);

            if (movie != null) {
                Picasso.with(this).load(posterBaseUrl + movie.posterPath).into(posterImageView);
                originalTitleTextView.setText(movie.originalTitle);
                releaseDateTextView.setText(new SimpleDateFormat("MMMM d, yyyy", Locale.US).format(movie.releaseDate));
                userRatingTextView.setText(String.valueOf(movie.userRating));
                plotSynopsisTitleTextView.setText(movie.plotSynopsis);
            }
        }
    }
}