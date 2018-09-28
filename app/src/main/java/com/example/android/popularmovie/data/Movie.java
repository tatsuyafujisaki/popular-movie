package com.example.android.popularmovie.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public final class Movie implements Parcelable {
    public final int id;

    @SerializedName("original_title")
    public final String originalTitle;

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("release_date")
    public final Date releaseDate;

    @SerializedName("vote_average")
    public final double userRating;

    @SerializedName("overview")
    public final String plotSynopsis;

    private Movie(Parcel parcel) {
        id = parcel.readInt();
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
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