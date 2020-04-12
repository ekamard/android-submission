package com.ekamard.mofyiv2.dataset;

import android.os.Parcel;
import android.os.Parcelable;

public class TvShows implements Parcelable {

    public static final Creator<TvShows> CREATOR = new Creator<TvShows>() {
        @Override
        public TvShows createFromParcel(Parcel in) {
            return new TvShows(in);
        }

        @Override
        public TvShows[] newArray(int size) {
            return new TvShows[size];
        }
    };
    private String title, caster, rating, synopsis, status, runtime, releaseInfo, genres, creator, network, trailerID, language, tvShowsId, seasons, episode, nextEpisode;
    private String imgPoster, imgBackdrop;

    public TvShows(Parcel in) {
        title = in.readString();
        caster = in.readString();
        rating = in.readString();
        synopsis = in.readString();
        status = in.readString();
        runtime = in.readString();
        releaseInfo = in.readString();
        genres = in.readString();
        creator = in.readString();
        network = in.readString();
        trailerID = in.readString();
        language = in.readString();
        imgPoster = in.readString();
        imgBackdrop = in.readString();
        tvShowsId = in.readString();
        seasons = in.readString();
        episode = in.readString();
        nextEpisode = in.readString();
    }

    public TvShows() {

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

    public String getReleaseInfo() {
        return releaseInfo;
    }

    public void setReleaseInfo(String releaseInfo) {
        this.releaseInfo = releaseInfo;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImgPoster() {
        return imgPoster;
    }

    public void setImgPoster(String imgPoster) {
        this.imgPoster = imgPoster;
    }

    public String getImgBackdrop() {
        return imgBackdrop;
    }

    public void setImgBackdrop(String imgBackdrop) {
        this.imgBackdrop = imgBackdrop;
    }

    public String getTvShowsId() {
        return tvShowsId;
    }

    public void setTvShowsId(String tvShowsId) {
        this.tvShowsId = tvShowsId;
    }

    public String getSeasons() {
        return seasons;
    }

    public void setSeasons(String seasons) {
        this.seasons = seasons;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getNextEpisode() {
        return nextEpisode;
    }

    public void setNextEpisode(String nextEpisode) {
        this.nextEpisode = nextEpisode;
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
        dest.writeString(runtime);
        dest.writeString(releaseInfo);
        dest.writeString(genres);
        dest.writeString(creator);
        dest.writeString(network);
        dest.writeString(trailerID);
        dest.writeString(language);
        dest.writeString(imgPoster);
        dest.writeString(imgBackdrop);
        dest.writeString(tvShowsId);
        dest.writeString(seasons);
        dest.writeString(episode);
        dest.writeString(nextEpisode);
    }
}
