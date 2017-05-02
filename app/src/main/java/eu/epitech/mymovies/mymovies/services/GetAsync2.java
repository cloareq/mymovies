package eu.epitech.mymovies.mymovies.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.epitech.mymovies.mymovies.R;
import eu.epitech.mymovies.mymovies.services.JSONParser;

/**
 * Created by Aurélien on 02/05/2017.
 */

public class GetAsync2 extends AsyncTask<String, String, JSONArray> {

    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected JSONArray doInBackground(String... args) {
        try {
            String URL = args[0];

            Log.d("request", "starting");

            JSONArray json = jsonParser.makeHttpRequest2(
                    URL, "GET", null);

            if (json != null) {
                return json;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray json) {
    }
}