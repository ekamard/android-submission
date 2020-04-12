package com.ekamard.mofyiv2.uiandvm.tvshows;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ekamard.mofyiv2.BuildConfig;
import com.ekamard.mofyiv2.dataset.TvShows;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class TvShowsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<TvShows>> listTvShowData = new MutableLiveData<>();
    private MutableLiveData<Boolean> connectionStatus = new MutableLiveData<>();

    private String urlImage = "https://image.tmdb.org/t/p/original";
    private String errorMessage = "Can't Load Data, Please check your internet connection..";

    public void setUpComingTvShow(final String myLanguage) {
        final ArrayList<TvShows> itemTvShowData = new ArrayList<>();
        final String myAPI_KEY = BuildConfig.TMDB_API_KEY;
        String url = "https://api.themoviedb.org/3/discover/tv?api_key=" + myAPI_KEY + "&=" + myLanguage;

        AsyncHttpClient myConnection = new AsyncHttpClient();
        myConnection.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    connectionStatus.postValue(true);
                    String responseObject = new String(responseBody);
                    JSONObject myJSONObject = new JSONObject(responseObject);
                    JSONArray listData = myJSONObject.getJSONArray("results");

                    for (int y = 0; y < listData.length(); y++) {
                        JSONObject dataTvShow = listData.getJSONObject(y);
                        TvShows upcomingTvShow = new TvShows();
                        String posterTvShow = dataTvShow.getString("poster_path");
                        upcomingTvShow.setImgPoster(urlImage + posterTvShow);
                        upcomingTvShow.setTitle(dataTvShow.getString("name"));
                        upcomingTvShow.setRating(dataTvShow.getString("vote_average"));
                        upcomingTvShow.setTvShowsId(dataTvShow.getString("id"));
                        itemTvShowData.add(upcomingTvShow);
                    }
                    listTvShowData.postValue(itemTvShowData);
                } catch (Exception e) {
                    Log.d("Excpetion", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailed", "" + errorMessage);
                connectionStatus.postValue(false);
            }
        });
    }

    LiveData<ArrayList<TvShows>> getTvShowData() {
        return listTvShowData;
    }

    LiveData<Boolean> getConnectionStatus() {
        return connectionStatus;
    }
}
