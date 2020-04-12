package com.ekamard.mofyiv2.uiandvm.detailmovies;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ekamard.mofyiv2.BuildConfig;
import com.ekamard.mofyiv2.dataset.Actor;
import com.ekamard.mofyiv2.dataset.Genres;
import com.ekamard.mofyiv2.dataset.Movies;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class MoviesDetailsViewModel extends ViewModel {

    private MutableLiveData<Movies> listDataMovies = new MutableLiveData<>();
    private MutableLiveData<Boolean> connectionStatus = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Genres>> listGenres = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Actor>> listActor = new MutableLiveData<>();

    private String myAPI_KEY = BuildConfig.TMDB_API_KEY;
    private String urlImage = "https://image.tmdb.org/t/p/original";
    private String errorMessage = "Can't Load Data, Please check your internet connection...";


    void setMoviesData(String moviesId, String myLanguage) {

        final String url = "https://api.themoviedb.org/3/movie/" + moviesId + "?api_key=" + myAPI_KEY + "&language=" + myLanguage;

        AsyncHttpClient myConnection = new AsyncHttpClient();
        Log.d("get movie id", "" + url);
        myConnection.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    connectionStatus.postValue(true);
                    String responseData = new String(responseBody);
                    JSONObject myJSONObject = new JSONObject(responseData);
                    Movies dataMoviesDetails = new Movies();
                    dataMoviesDetails.setTitle(myJSONObject.getString("title"));
                    String runTime = myJSONObject.getString("runtime");
                    dataMoviesDetails.setRuntime(runTime + "m");
                    dataMoviesDetails.setSynopsis(myJSONObject.getString("overview"));
                    String poster = myJSONObject.getString("poster_path");
                    dataMoviesDetails.setImgPoster(urlImage + poster);
                    String imageBackDrop = myJSONObject.getString("backdrop_path");
                    dataMoviesDetails.setImgBackdrop(urlImage + imageBackDrop);
                    dataMoviesDetails.setRating(myJSONObject.getString("vote_average"));
                    dataMoviesDetails.setStatus(myJSONObject.getString("status"));
                    dataMoviesDetails.setReleaseInfo(myJSONObject.getString("release_date"));
                    dataMoviesDetails.setLanguage(myJSONObject.getString("original_language"));
                    listDataMovies.postValue(dataMoviesDetails);
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

    void setGenresData(String moviesId, String myLanguage) {
        final String url = "https://api.themoviedb.org/3/movie/" + moviesId + "?api_key=" + myAPI_KEY + "&language=" + myLanguage;
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

    public LiveData<Boolean> getConnectionStatus() {
        return connectionStatus;
    }

    LiveData<Movies> getMoviesData() {
        return listDataMovies;
    }

    LiveData<ArrayList<Genres>> getGenresData() {
        return listGenres;
    }

    LiveData<ArrayList<Actor>> getActorData() {
        return listActor;
    }

    void setActorData(String moviesId) {
        final String url = "https://api.themoviedb.org/3/movie/" + moviesId + "/credits?api_key=" + myAPI_KEY;
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
