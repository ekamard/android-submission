package com.ekamard.mofyiv2.dataset;

import android.os.Parcel;
import android.os.Parcelable;

public class Movies implements Parcelable {
    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
    private String title, caster, rating, synopsis, status, director, runtime, releaseInfo, trailerID, language, moviesId;
    private String imgPoster, imgBackdrop;

    public Movies(Parcel in) {
        title = in.readString();
        caster = in.readString();
        rating = in.readString();
        synopsis = in.readString();
        status = in.readString();
        director = in.readString();
        runtime = in.readString();
        releaseInfo = in.readString();
        trailerID = in.readString();
        language = in.readString();
        imgPoster = in.readString();
        imgBackdrop = in.readString();
        moviesId = in.readString();
    }

    public Movies() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getReleaseInfo() {
        return releaseInfo;
    }

    public void setReleaseInfo(String releaseInfo) {
        this.releaseInfo = releaseInfo;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImgBackdrop() {
        return imgBackdrop;
    }

    public void setImgBackdrop(String imgBackdrop) {
        this.imgBackdrop = imgBackdrop;
    }


    public String getImgPoster() {
        return imgPoster;
    }

    public void setImgPoster(String imgPoster) {
        this.imgPoster = imgPoster;
    }

    public String getMoviesId() {
        return moviesId;
    }

    public void setMoviesId(String moviesId) {
        this.moviesId = moviesId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(caster);
        dest.writeString(rating);
        dest.writeString(synopsis);
        dest.writeString(status);
        dest.writeString(director);
        dest.writeString(runtime);
        dest.writeString(releaseInfo);
        dest.writeString(trailerID);
        dest.writeString(language);
        dest.writeString(imgPoster);
        dest.writeString(imgBackdrop);
        dest.writeString(moviesId);
    }
}
