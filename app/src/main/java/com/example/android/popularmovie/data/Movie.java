package com.example.android.popularmovie.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

@Entity
public final class Movie implements Parcelable {
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

    @PrimaryKey
    public final int id;

    @SerializedName("original_title")
    public final String originalTitle;

    @SerializedName("release_date")
    public final LocalDate releaseDate;

    @SerializedName("vote_average")
    public final double voteAverage;

    public final String overview;

    @SerializedName("poster_path")
    public String posterPath;

    Movie(int id, String originalTitle, String posterPath, LocalDate releaseDate, double voteAverage, String overview) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    @Ignore
    private Movie(Parcel parcel) {
        id = parcel.readInt();
        originalTitle = parcel.readString();
        posterPath = parcel.readString();
        releaseDate = (LocalDate) parcel.readSerializable();
        voteAverage = parcel.readDouble();
        overview = parcel.readString();
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
        parcel.writeDouble(voteAverage);
        parcel.writeString(overview);
    }
}