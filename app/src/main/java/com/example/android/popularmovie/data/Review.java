package com.example.android.popularmovie.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Objects;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Movie.class,
        parentColumns = "id",
        childColumns = "movie_id",
        onDelete = CASCADE,
        onUpdate = CASCADE),
        indices = { @Index("movie_id") })
public final class Review implements Parcelable {
    @PrimaryKey
    @NonNull
    public String id;

    public String author;
    public String content;
    public String url;

    @ColumnInfo(name = "movie_id")
    public int movieId;

    public Review(){
    }

    private Review(Parcel parcel) {
        id = Objects.requireNonNull(parcel.readString());
        author = parcel.readString();
        content = parcel.readString();
        url  = parcel.readString();
    }

    /*
     * implements Parcelable
     */

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
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }
    };
}