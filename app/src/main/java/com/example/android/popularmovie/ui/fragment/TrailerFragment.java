package com.example.android.popularmovie.ui.fragment;

import android.arch.lifecycle.LiveData;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovie.R;
import com.example.android.popularmovie.databinding.FragmentTrailerBinding;
import com.example.android.popularmovie.databinding.TrailerViewHolderBinding;
import com.example.android.popularmovie.room.entity.Movie;
import com.example.android.popularmovie.room.entity.Trailer;
import com.example.android.popularmovie.util.ApiResponse;
import com.example.android.popularmovie.util.ui.IntentUtils;
import com.example.android.popularmovie.viewmodel.MovieViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.example.android.popularmovie.ui.activity.DetailActivity.MOVIE_PARCELABLE_EXTRA_KEY;

public class TrailerFragment extends Fragment {
    @Inject
    MovieViewModel movieViewModel;

    private Context context;
    private FragmentTrailerBinding binding;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
        this.context = context;s
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTrailerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ApiResponse<LiveData<List<Trailer>>> response = movieViewModel.getTrailers(((Movie) IntentUtils.getParcelableExtra(this, MOVIE_PARCELABLE_EXTRA_KEY)).id);

        if (response.isSuccessful) {
            response.data.observe(this, trailers -> {
                if (!Objects.requireNonNull(trailers).isEmpty()) {
                    binding.recyclerView.setAdapter(new Adapter(trailers));
                }
            });
        }
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        private final List<Trailer> trailers;

        private Adapter(List<Trailer> trailers) {
            this.trailers = trailers;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(TrailerViewHolderBinding.inflate(LayoutInflater.from(context), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Trailer trailer = trailers.get(position);

            holder.binding.trailerLabelTextView.setText(trailer.name);
            holder.binding.trailerInfoTextView.setText(context.getString(R.string.trailer_info_format, trailer.iso639, trailer.iso3166, trailer.size));
        }

        @Override
        public int getItemCount() {
            return trailers.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            final TrailerViewHolderBinding binding;

            ViewHolder(TrailerViewHolderBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                binding.getRoot().setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Trailer trailer = trailers.get(getAdapterPosition());

                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.youtube_base_uri).concat(trailer.key))));
                } catch (ActivityNotFoundException e) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.youtube_base_url).concat(trailer.key))));
                }
            }
        }
    }
}