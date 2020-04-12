package com.ekamard.mofyiv2.uiandvm.actor;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ekamard.mofyiv2.BuildConfig;
import com.ekamard.mofyiv2.dataset.Actor;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class ActorsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Actor>> listOfActor = new MutableLiveData<>();
    private MutableLiveData<Boolean> connectionStatus = new MutableLiveData<>();
    private String urlImage = "https://image.tmdb.org/t/p/original";
    private String errorMessage = "Can't Load Data, Please check your internet connection..";

    public void setActorData() {
        String myAPI_KEY = BuildConfig.TMDB_API_KEY;
        final String url = "https://api.themoviedb.org/3/person/popular?api_key=" + myAPI_KEY + "&language=en-US";
        final ArrayList<Actor> itemDataActor = new ArrayList<>();

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
                        JSONObject dataActor = listData.getJSONObject(x);
                        Actor popularActor = new Actor();
                        String imgProfile = dataActor.getString("profile_path");
                        popularActor.setProfileImage(urlImage + imgProfile);
                        popularActor.setName(dataActor.getString("name"));
                        itemDataActor.add(popularActor);
                    }
                    listOfActor.postValue(itemDataActor);
                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("onFailedConnection", "" + errorMessage);
                connectionStatus.postValue(false);
            }
        });

    }

    LiveData<ArrayList<Actor>> getActorData() {
        return listOfActor;
    }

    LiveData<Boolean> getConnectionStatus() {
        return connectionStatus;
    }
}
