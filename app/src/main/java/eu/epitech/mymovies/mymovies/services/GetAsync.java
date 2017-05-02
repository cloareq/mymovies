package eu.epitech.mymovies.mymovies.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import eu.epitech.mymovies.mymovies.R;
import eu.epitech.mymovies.mymovies.services.JSONParser;

public class GetAsync extends AsyncTask<String, String, JSONObject> {

    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected JSONObject doInBackground(String... args) {
        try {
            String URL = args[0];

            Log.d("request", "starting");

            JSONObject json = jsonParser.makeHttpRequest(
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
    protected void onPostExecute(JSONObject json) {
    }
}