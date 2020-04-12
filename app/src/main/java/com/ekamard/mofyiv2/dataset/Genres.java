package com.ekamard.mofyiv2.dataset;

import android.os.Parcel;
import android.os.Parcelable;

public class Genres implements Parcelable {

    public static final Creator<Genres> CREATOR = new Creator<Genres>() {
        @Override
        public Genres createFromParcel(Parcel in) {
            return new Genres(in);
        }

        @Override
        public Genres[] newArray(int size) {
            return new Genres[size];
        }
    };
    private String name;
    private String idGenre;

    private Genres(Parcel in) {
        name = in.readString();
        idGenre = in.readString();
    }

    public Genres() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(idGenre);
    }
}
