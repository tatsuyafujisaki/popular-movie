package com.example.android.popularmovie.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

@Entity
public final class Movie implements Parcelable {
    @PrimaryKey
    private  int id;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("original_title")
    private  String originalTitle;

    @SerializedName("release_date")
    private LocalDate releaseDate;

    @SerializedName("vote_average")
    private double voteAverage;

    private String overview;

    public Movie(){
    }

    private Movie(Parcel parcel) {
        id = parcel.readInt();
        originalTitle = parcel.readString();
        posterPath = parcel.readString();
        releaseDate = (LocalDate) parcel.readSerializable();
        voteAverage = parcel.readDouble();
        overview = parcel.readString();
    }

    /*
     * Getter
     */

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    /*
     * Setter
     */

    void setId(int id) {
        this.id = id;
    }

    void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    /*
     * implements Parcelable
     */

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