package com.ekamard.mofyiv2.dataset;

import android.os.Parcel;
import android.os.Parcelable;

public class Actor implements Parcelable {

    public static final Creator<Actor> CREATOR = new Creator<Actor>() {
        @Override
        public Actor createFromParcel(Parcel in) {
            return new Actor(in);
        }

        @Override
        public Actor[] newArray(int size) {
            return new Actor[size];
        }
    };
    private String name, birthday, gender, profileImage, deathday, biography, placeOfBirth;
    private Integer actor_id, imdb_id, gender_id, popularity;

    private Actor(Parcel in) {
        name = in.readString();
        birthday = in.readString();
        gender = in.readString();
        profileImage = in.readString();
        deathday = in.readString();
        biography = in.readString();
        placeOfBirth = in.readString();
        if (in.readByte() == 0) {
            actor_id = null;
        } else {
            actor_id = in.readInt();
        }
        if (in.readByte() == 0) {
            imdb_id = null;
        } else {
            imdb_id = in.readInt();
        }
        if (in.readByte() == 0) {
            gender_id = null;
        } else {
            gender_id = in.readInt();
        }
        if (in.readByte() == 0) {
            popularity = null;
        } else {
            popularity = in.readInt();
        }
    }

    public Actor() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(birthday);
        dest.writeString(gender);
        dest.writeString(profileImage);
        dest.writeString(deathday);
        dest.writeString(biography);
        dest.writeString(placeOfBirth);
        if (actor_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(actor_id);
        }
        if (imdb_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(imdb_id);
        }
        if (gender_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(gender_id);
        }
        if (popularity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(popularity);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


}
