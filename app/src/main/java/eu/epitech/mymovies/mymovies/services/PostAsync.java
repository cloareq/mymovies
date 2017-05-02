package eu.epitech.mymovies.mymovies.services;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

public class PostAsync extends AsyncTask<String, String, JSONObject> {

    JSONParser jsonParser = new JSONParser();
    String LOGIN_URL;


    @Override
    protected void onPreExecute() {
    }

    @Override
    protected JSONObject doInBackground(String... args) {

        try {
            LOGIN_URL = args[0];
            HashMap<String, String> params = new HashMap<>();
            params.put("idfb", args[1]);
            params.put("name", args[2]);

            Log.d("request", "starting");

            JSONObject json = jsonParser.makeHttpRequest(
                    LOGIN_URL, "POST", params);

            if (json != null) {
                System.out.println("JSON - > " + json);
                return json;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(JSONObject json) {
    }

}