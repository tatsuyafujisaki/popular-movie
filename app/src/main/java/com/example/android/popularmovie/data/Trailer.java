package com.example.android.popularmovie.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Movie.class,
        parentColumns = "id",
        childColumns = "movie_id",
        onDelete = CASCADE,
        onUpdate = CASCADE),
        indices = {@Index("movie_id")})
public final class Trailer implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String size;
    public String source;
    public String type;

    @ColumnInfo(name = "movie_id")
    public int movieId;

    Trailer() {
    }

    private Trailer(Parcel parcel) {
        id = parcel.readInt();
        name = parcel.readString();
        size = parcel.readString();
        source = parcel.readString();
        type = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(size);
        dest.writeString(source);
        dest.writeString(type);
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}