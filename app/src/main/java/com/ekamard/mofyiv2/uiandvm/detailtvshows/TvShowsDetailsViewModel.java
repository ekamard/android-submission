package com.ekamard.mofyiv2.uiandvm.detailtvshows;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ekamard.mofyiv2.BuildConfig;
import com.ekamard.mofyiv2.dataset.Actor;
import com.ekamard.mofyiv2.dataset.Genres;
import com.ekamard.mofyiv2.dataset.TvShows;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class TvShowsDetailsViewModel extends ViewModel {

    private MutableLiveData<TvShows> listDataTvShows = new MutableLiveData<>();
    private MutableLiveData<Boolean> connectionStatus = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Genres>> listGenres = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Actor>> listActor = new MutableLiveData<>();

    private String myAPI_KEY = BuildConfig.TMDB_API_KEY;
    private String urlImage = "https://image.tmdb.org/t/p/original";
    private String errorMessage = "Can't Load Data, Please check your internet connection...";

    public void setTvShowsData(final String tvShowsId, String myLanguage) {
        final String url = "https://api.themoviedb.org/3/tv/" + tvShowsId + "?api_key=" + myAPI_KEY + "&language=" + myLanguage;

        AsyncHttpClient myConnection = new AsyncHttpClient();
        myConnection.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String title = null;
                String poster = null;
                String backdrop = null;
                String synopsis = null;
                String rating = null;
                String status = null;
                String releaseDate = null;
                String language = null;
                String episode = null;
                String seasons = null;
                String nextEpisode = null;
                String end = "Ended";

                try {
                    connectionStatus.postValue(true);
                    String responseData = new String(responseBody);
                    JSONObject tvObject = new JSONObject(responseData);

                    TvShows tvShowsDetailsData = new TvShows();
                    title = tvObject.getString("name");
                    poster = tvObject.getString("poster_path");
                    backdrop = tvObject.getString("backdrop_path");
                    synopsis = tvObject.getString("overview");
                    rating = tvObject.getString("vote_average");
                    status = tvObject.getString("status");
                    releaseDate = tvObject.getString("last_air_date");
                    language = tvObject.getString("original_language");
                    episode = tvObject.getString("number_of_episodes");
                    seasons = tvObject.getString("number_of_seasons");
                    nextEpisode = tvObject.getJSONObject("last_episode_to_air").getString("air_Date");

                    tvShowsDetailsData.setTitle(title);
                    tvShowsDetailsData.setImgPoster(urlImage + poster);
                    tvShowsDetailsData.setImgBackdrop(urlImage + backdrop);
                    tvShowsDetailsData.setSynopsis(synopsis);
                    tvShowsDetailsData.setRating(rating);
                    tvShowsDetailsData.setStatus(status);
                    tvShowsDetailsData.setReleaseInfo(releaseDate);
                    tvShowsDetailsData.setLanguage(language);
                    tvShowsDetailsData.setEpisode(episode);
                    tvShowsDetailsData.setSeasons(seasons);
                    tvShowsDetailsData.setNextEpisode(nextEpisode);

                    listDataTvShows.postValue(tvShowsDetailsData);
                } catch (Exception e) {
                    Log.d("Exception e", Objects.requireNonNull(e.getMessage()));
                    TvShows tvShowsDetailsData = new TvShows();
                    tvShowsDetailsData.setTitle(title);
                    tvShowsDetailsData.setImgPoster(urlImage + poster);
                    tvShowsDetailsData.setImgBackdrop(urlImage + backdrop);
                    tvShowsDetailsData.setSynopsis(synopsis);
                    tvShowsDetailsData.setRating(rating);
                    tvShowsDetailsData.setStatus(status);
                    tvShowsDetailsData.setReleaseInfo(releaseDate);
                    tvShowsDetailsData.setLanguage(language);
                    tvShowsDetailsData.setEpisode(episode);
                    tvShowsDetailsData.setSeasons(seasons);
                    tvShowsDetailsData.setNextEpisode(nextEpisode);
                    tvShowsDetailsData.setNextEpisode(end);
                    listDataTvShows.postValue(tvShowsDetailsData);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                connectionStatus.postValue(false);
                Log.e("onFailed", "" + errorMessage);
            }
        });
    }

    void setGenresData(String tvShowsId, String myLanguage) {
        final String url = "https://api.themoviedb.org/3/tv/" + tvShowsId + "?api_key=" + myAPI_KEY + "&language=" + myLanguage;
        final ArrayList<Genres> listOfGenres = new ArrayList<>();

        AsyncHttpClient myConnection = new AsyncHttpClient();
        myConnection.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String responseData = new String(responseBody);
                    JSONObject myJSONObject = new JSONObject(responseData);
                    JSONArray listDataGenres = myJSONObject.getJSONArray("genres");
                    for (int x = 0; x < listDataGenres.length(); x++) {
                        JSONObject dataGenre = listDataGenres.getJSONObject(x);
                        Genres myGenres = new Genres();
                        myGenres.setName(dataGenre.getString("name"));
                        listOfGenres.add(myGenres);
                    }
                    listGenres.postValue(listOfGenres);

                } catch (Exception e) {
                    Log.d("Exception e ", "" + errorMessage);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("onFailed", "" + errorMessage);
                // connectionStatus.postValue(false);
            }
        });
    }

    LiveData<Boolean> getConnectionStatus() {
        return connectionStatus;
    }

    LiveData<TvShows> getTvShowsData() {
        return listDataTvShows;
    }

    LiveData<ArrayList<Genres>> getGenresData() {
        return listGenres;
    }

    LiveData<ArrayList<Actor>> getActorData() {
        return listActor;
    }

    void setActorData(String tvShowsId) {
        final String url = "https://api.themoviedb.org/3/tv/" + tvShowsId + "/credits?api_key=" + myAPI_KEY + "&language=en-US";
        final ArrayList<Actor> listDataActor = new ArrayList<>();

        AsyncHttpClient myConnection = new AsyncHttpClient();
        myConnection.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    connectionStatus.postValue(true);
                    String responseData = new String(responseBody);
                    JSONObject objectActor = new JSONObject(responseData);
                    JSONArray dataActor = objectActor.getJSONArray("cast");

                    for (int z = 0; z < dataActor.length(); z++) {
                        JSONObject getDataActor = dataActor.getJSONObject(z);
                        Actor listActor = new Actor();
                        listActor.setName(getDataActor.getString("name"));
                        String profileImg = getDataActor.getString("profile_path");
                        listActor.setProfileImage(urlImage + profileImg);
                        listDataActor.add(listActor);
                    }
                    listActor.postValue(listDataActor);
                } catch (Exception e) {
                    Log.d("Exception e", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                connectionStatus.postValue(false);
                Log.e("onFailed : ", "" + errorMessage);
            }
        });
    }
}
