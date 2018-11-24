package com.example.android.popularmovie.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Pair;

import com.example.android.popularmovie.R;
import com.example.android.popularmovie.ui.fragment.OverviewFragment;
import com.example.android.popularmovie.ui.fragment.ReviewFragment;
import com.example.android.popularmovie.ui.fragment.TrailerFragment;

import java.util.Arrays;
import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private final Context context;
    private final List<Pair<Fragment, Integer>> pairs =
            Arrays.asList(Pair.create(new OverviewFragment(), R.string.overview_tab),
                    Pair.create(new TrailerFragment(), R.string.trailers_tab),
                    Pair.create(new ReviewFragment(), R.string.reviews_tabs));

    public MyFragmentPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return pairs.get(position).first;
    }

    @Override
    public int getCount() {
        return pairs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(pairs.get(position).second);
    }
}