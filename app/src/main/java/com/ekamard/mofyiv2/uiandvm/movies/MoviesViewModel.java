package com.ekamard.mofyiv2.uiandvm.movies;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ekamard.mofyiv2.BuildConfig;
import com.ekamard.mofyiv2.dataset.Movies;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class MoviesViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Movies>> listMoviesData = new MutableLiveData<>();
    private MutableLiveData<Boolean> connectionStatus = new MutableLiveData<>();
    private String urlImage = "https://image.tmdb.org/t/p/original";
    private String errorMessage = "Can't Load Data, Please check your internet connection..";

    LiveData<ArrayList<Movies>> getMoviesData() {
        return listMoviesData;
    }

    public void setMoviesData(final String myLanguage) {
        final ArrayList<Movies> itemMoviesData = new ArrayList<>();
        final String myAPI_KEY = BuildConfig.TMDB_API_KEY;
        final String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + myAPI_KEY + "&language=" + myLanguage;

        AsyncHttpClient myConnection = new AsyncHttpClient();
        myConnection.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    connectionStatus.postValue(true);
                    String responseData = new String(responseBody);
                    JSONObject myJSONObject = new JSONObject(responseData);
                    JSONArray listData = myJSONObject.getJSONArray("results");

                    for (int x = 0; x < listData.length(); x++) {
                        JSONObject dataMovies = listData.getJSONObject(x);
                        Movies upcomingMovies = new Movies();
                        String imgPoster = dataMovies.getString("poster_path");
                        upcomingMovies.setImgPoster(urlImage + imgPoster);
                        upcomingMovies.setTitle(dataMovies.getString("title"));
                        upcomingMovies.setRating(dataMovies.getString("vote_average"));
                        String idMovies = dataMovies.getString("id");
                        Log.d("getData", "" + idMovies);
                        upcomingMovies.setMoviesId(idMovies);
                        itemMoviesData.add(upcomingMovies);

                    }
                    listMoviesData.postValue(itemMoviesData);
                    Log.d("changeLang", "" + url);
                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("onFailed", "" + errorMessage);
                connectionStatus.postValue(false);
            }
        });
    }

    LiveData<Boolean> getConnectionStatus() {
        return connectionStatus;
    }
}
