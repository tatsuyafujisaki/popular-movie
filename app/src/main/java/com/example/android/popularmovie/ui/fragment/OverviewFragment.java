package com.example.android.popularmovie.ui.fragment;

import android.content.Context;
import androidx.databinding.BindingAdapter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovie.databinding.FragmentOverviewBinding;
import com.example.android.popularmovie.util.ui.IntentUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dagger.android.support.AndroidSupportInjection;

import static com.example.android.popularmovie.ui.activity.DetailActivity.MOVIE_PARCELABLE_EXTRA_KEY;

public class OverviewFragment extends Fragment {
    private FragmentOverviewBinding binding;

    @BindingAdapter("android:text")
    public static void setDoubleToTextView(TextView textView, Double d) {
        textView.setText(String.valueOf(d));
    }

    @BindingAdapter("android:text")
    public static void setDateToTextView(TextView textView, Date date) {
        textView.setText(new SimpleDateFormat("MMMM d, yyyy", Locale.US).format(date));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setMovie(IntentUtils.getParcelableExtra(this, MOVIE_PARCELABLE_EXTRA_KEY));
    }
}