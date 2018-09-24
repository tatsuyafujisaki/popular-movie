package com.example.android.popularmovie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

final class PosterAdapter extends ArrayAdapter<Movie> {

    @BindView(R.id.poster_thumbnail_view)
    ImageView posterThumbnailView;

    @BindString(R.string.poster_base_url)
    String posterBaseUrl;

    PosterAdapter(Context context, List<Movie> movie) {
        super(context, 0, movie);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }

        ButterKnife.bind(this, convertView);

        Movie movie = getItem(position);

        if (movie != null) {
            Picasso.get().load(posterBaseUrl + movie.posterPath).into(posterThumbnailView);
        }

        return convertView;
    }
}