package com.example.android.popularmovie;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovie.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();

        String intentExtraKey = getString(R.string.intent_extra_key);

        if (bundle != null && bundle.containsKey(intentExtraKey)) {
            binding.setMovie(bundle.getParcelable(intentExtraKey));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView imageView, String posterPath) {
        Picasso.get().load(posterPath).into(imageView);
    }

    @BindingAdapter("android:text")
    public static void setText(TextView textView, Date releaseDate) {
        textView.setText(new SimpleDateFormat("MMMM d, yyyy", Locale.US).format(releaseDate));
    }
}