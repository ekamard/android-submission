package com.ekamard.mofyiv2.uiandvm.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ekamard.mofyiv2.BuildConfig;
import com.ekamard.mofyiv2.dataset.Movies;
import com.ekamard.mofyiv2.dataset.TvShows;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class HomeViewModel extends ViewModel {

    private String myAPI_KEY = BuildConfig.TMDB_API_KEY;
    private MutableLiveData<ArrayList<Movies>> listMoviesMutable = new MutableLiveData<>();
    private MutableLiveData<ArrayList<TvShows>> listTvShowMutable = new MutableLiveData<>();
    private MutableLiveData<Boolean> connectionStatus = new MutableLiveData<>();
    private String urlImage = "https://image.tmdb.org/t/p/original";
    private String errorMessage = "Can't Load Data, Please check your internet connection..";

     void setUpcomingMovies() {
        final ArrayList<Movies> listMoviesItem = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + myAPI_KEY + "&language=en-US";

        final AsyncHttpClient myConnection = new AsyncHttpClient();
        myConnection.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    connectionStatus.postValue(true);
                    String resultData = new String(responseBody);
                    JSONObject responseObject = new JSONObject(resultData);
                    JSONArray listData = responseObject.getJSONArray("results");

                    for (int x = 0; x < listData.length(); x++) {

                        //pasrsing dta JSON
                        JSONObject dataMovies = listData.getJSONObject(x);
                        Movies upcomingMovies = new Movies();
                        upcomingMovies.setTitle(dataMovies.getString("title"));
                        upcomingMovies.setRating(dataMovies.getString("vote_average"));
                        String posterPath = dataMovies.getString("poster_path");
                        upcomingMovies.setImgPoster(urlImage + posterPath);
                        upcomingMovies.setMoviesId(dataMovies.getString("id"));
                        listMoviesItem.add(upcomingMovies);
                    }
                    listMoviesMutable.postValue(listMoviesItem);

                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailed", "" + errorMessage);
                connectionStatus.postValue(false);
            }
        });
    }

    LiveData<ArrayList<Movies>> getMovies() {
        return listMoviesMutable;
    }

    void setUpcomingTvShow() {
        final ArrayList<TvShows> listTvShowsItem = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/tv?api_key=" + myAPI_KEY + "&language=en-US";

        AsyncHttpClient myConnection = new AsyncHttpClient();
        myConnection.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String resultTvShowData = new String(responseBody);
                    JSONObject responseObject = new JSONObject(resultTvShowData);
                    JSONArray listTvShowData = responseObject.getJSONArray("results");

                    for (int y = 0; y < listTvShowData.length(); y++) {
                        JSONObject dataTvShows = listTvShowData.getJSONObject(y);
                        TvShows upcomingTvShows = new TvShows();

                        upcomingTvShows.setTitle(dataTvShows.getString("name"));
                        upcomingTvShows.setRating(dataTvShows.getString("vote_average"));
                        String posterTvShow = dataTvShows.getString("poster_path");
                        upcomingTvShows.setImgPoster(urlImage + posterTvShow);
                        upcomingTvShows.setTvShowsId(dataTvShows.getString("id"));
                        listTvShowsItem.add(upcomingTvShows);
                    }
                    listTvShowMutable.postValue(listTvShowsItem);
                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailed", "" + errorMessage);
                connectionStatus.postValue(false);
            }
        });
    }

    LiveData<ArrayList<TvShows>> getTvshow() {
        return listTvShowMutable;
    }

    LiveData<Boolean> getConnectionStatus() {
        return connectionStatus;
    }
}
