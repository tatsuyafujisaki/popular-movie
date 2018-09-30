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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DetailActivity extends AppCompatActivity {
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

        Bundle bundle = getIntent().getExtras();

        String intentExtraKey = getString(R.string.intent_extra_key);

        if (bundle != null && bundle.containsKey(intentExtraKey)) {
            ((ActivityDetailBinding) DataBindingUtil.setContentView(this, R.layout.activity_detail)).setMovie(bundle.getParcelable(intentExtraKey));
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
}