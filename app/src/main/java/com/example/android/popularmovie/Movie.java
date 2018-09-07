package com.example.android.popularmovie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Movie implements Parcelable {

    @SerializedName("original_title")
    final String originalTitle;

    @SerializedName("poster_path")
    final String posterPath;

    @SerializedName("release_date")
    final Date releaseDate;

    @SerializedName("vote_average")
    final double userRating;

    @SerializedName("overview")
    final String plotSynopsis;

    private Movie(Parcel parcel) {
        originalTitle = parcel.readString();
        posterPath = parcel.readString();
        releaseDate = (Date) parcel.readSerializable();
        userRating = parcel.readDouble();
        plotSynopsis = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return String.join(",", originalTitle, posterPath, new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(releaseDate), String.valueOf(userRating), plotSynopsis);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(originalTitle);
        parcel.writeString(posterPath);
        parcel.writeSerializable(releaseDate);
        parcel.writeDouble(userRating);
        parcel.writeString(plotSynopsis);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}