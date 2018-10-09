package com.example.android.popularmovie.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

@Entity
public final class Movie implements Parcelable {
    @PrimaryKey
    public int id;

    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    public String posterPath;

    @SerializedName("original_title")
    @ColumnInfo(name = "original_title")
    public String originalTitle;

    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    public LocalDate releaseDate;

    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    public double voteAverage;

    public String overview;

    @ColumnInfo(name = "is_popular")
    public boolean isPopular;

    @ColumnInfo(name = "is_top_rated")
    public boolean isTopRated;

    @ColumnInfo(name = "is_favorite")
    public boolean isFavorite;

    public Movie(){
    }

    private Movie(Parcel parcel) {
        id = parcel.readInt();
        originalTitle = parcel.readString();
        posterPath = parcel.readString();
        releaseDate = (LocalDate) parcel.readSerializable();
        voteAverage = parcel.readDouble();
        overview = parcel.readString();
        isPopular = parcel.readInt() != 0;
        isTopRated = parcel.readInt() != 0;
        isFavorite = parcel.readInt() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(originalTitle);
        parcel.writeString(posterPath);
        parcel.writeSerializable(releaseDate);
        parcel.writeDouble(voteAverage);
        parcel.writeString(overview);
        parcel.writeInt(isPopular ? 1 : 0);
        parcel.writeInt(isTopRated ? 1 : 0);
        parcel.writeInt(isFavorite ? 1 : 0);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}