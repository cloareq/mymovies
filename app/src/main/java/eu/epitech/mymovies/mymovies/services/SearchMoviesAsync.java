package eu.epitech.mymovies.mymovies.services;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class SearchMoviesAsync extends AsyncTask<String, String, JSONArray> {

    JSONParser jsonParser = new JSONParser();
    String URL = "http://landswar.com:3000/movies/search";


    @Override
    protected void onPreExecute() {
    }

    @Override
    protected JSONArray doInBackground(String... args) {
        try {
            HashMap<String, String> params = new HashMap<>();
            Log.d("IDFB", args[0]);
            Log.d("Search", args[1]);
            params.put("idfb", args[0]);
            params.put("search", args[1]);

            Log.d("request", "starting");

            JSONArray json = jsonParser.makeHttpRequest2(
                    URL, "POST", params);

            if (json != null) {
                return json;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(JSONArray json) {
    }

}