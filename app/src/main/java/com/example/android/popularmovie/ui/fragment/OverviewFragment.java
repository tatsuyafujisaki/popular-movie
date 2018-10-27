package com.example.android.popularmovie.ui.fragment;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovie.databinding.FragmentOverviewBinding;
import com.example.android.popularmovie.util.IntentUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dagger.android.support.AndroidSupportInjection;

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
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setMovie(IntentUtils.getParcelableExtra(this));
    }
}