package com.example.android.popularmovie.room.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Movie.class,
        parentColumns = "id",
        childColumns = "movie_id",
        onDelete = CASCADE,
        onUpdate = CASCADE),
        indices = {@Index("movie_id")})
public class Review implements Parcelable {
    @PrimaryKey
    @NonNull
    public String id;

    public String author;
    public String content;
    public String url;

    @ColumnInfo(name = "movie_id")
    public int movieId;

    public Review() {
    }

    private Review(Parcel parcel) {
        id = Objects.requireNonNull(parcel.readString());
        author = parcel.readString();
        content = parcel.readString();
        url = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}