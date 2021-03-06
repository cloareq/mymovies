package eu.epitech.mymovies.mymovies.services;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.epitech.mymovies.mymovies.Models.Movies;

public class JSONParser {

    String charset = "UTF-8";
    HttpURLConnection conn;
    DataOutputStream wr;
    StringBuilder result;
    URL urlObj;
    JSONObject jObj = null;
    JSONArray jArray = null;
    StringBuilder sbParams;
    String paramsString;

    public JSONObject makeHttpRequest(String url, String method,
                                      HashMap<String, String> params) {

        sbParams = new StringBuilder();
        int i = 0;
        if (params != null) {
            for (String key : params.keySet()) {
                try {
                    if (i != 0) {
                        sbParams.append("&");
                    }
                    sbParams.append(key).append("=")
                            .append(URLEncoder.encode(params.get(key), charset));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }

        if (method.equals("POST")) {
            try {
                urlObj = new URL(url);

                conn = (HttpURLConnection) urlObj.openConnection();

                conn.setDoOutput(true);

                conn.setRequestMethod("POST");

                conn.setRequestProperty("Accept-Charset", charset);

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                conn.connect();

                paramsString = sbParams.toString();

                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(paramsString);
                wr.flush();
                wr.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("GET")){

            try {
                urlObj = new URL(url);

                conn = (HttpURLConnection) urlObj.openConnection();

                conn.setDoOutput(false);

                conn.setRequestMethod("GET");

                conn.setRequestProperty("Accept-Charset", charset);

                conn.setConnectTimeout(15000);

                conn.connect();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (method.equals("PUT")) {
            try {
                urlObj = new URL(url);

                conn = (HttpURLConnection) urlObj.openConnection();

                conn.setDoOutput(true);

                conn.setRequestMethod("PUT");

                conn.setRequestProperty("Accept-Charset", charset);

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                conn.connect();

                paramsString = sbParams.toString();

                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(paramsString);
                wr.flush();
                wr.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            //Receive the response from the server
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d("JSON Parser", "result: " + result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.disconnect();

        // try parse the string to a JSON object

        try {
            jObj = new JSONObject(result.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON Object
        return jObj;
    }

    public JSONArray makeHttpRequest2(String url, String method,
                                      HashMap<String, String> params) {

        sbParams = new StringBuilder();
        int i = 0;
        if (params != null) {
            for (String key : params.keySet()) {
                try {
                    if (i != 0) {
                        sbParams.append("&");
                    }
                    sbParams.append(key).append("=")
                            .append(URLEncoder.encode(params.get(key), charset));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }

        if (method.equals("POST")) {
            try {
                urlObj = new URL(url);

                conn = (HttpURLConnection) urlObj.openConnection();

                conn.setDoOutput(true);

                conn.setRequestMethod("POST");

                conn.setRequestProperty("Accept-Charset", charset);

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                conn.connect();

                paramsString = sbParams.toString();

                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(paramsString);
                wr.flush();
                wr.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("GET")){

            try {
                urlObj = new URL(url);

                conn = (HttpURLConnection) urlObj.openConnection();

                conn.setDoOutput(false);

                conn.setRequestMethod("GET");

                conn.setRequestProperty("Accept-Charset", charset);

                conn.setConnectTimeout(15000);

                conn.connect();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (method.equals("PUT")) {
            try {
                urlObj = new URL(url);

                conn = (HttpURLConnection) urlObj.openConnection();

                conn.setDoOutput(true);

                conn.setRequestMethod("PUT");

                conn.setRequestProperty("Accept-Charset", charset);

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                conn.connect();

                paramsString = sbParams.toString();

                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(paramsString);
                wr.flush();
                wr.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            //Receive the response from the server
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d("JSON Parser", "result: " + result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.disconnect();

        // try parse the string to a JSON object

        try {
            jArray = new JSONArray(result.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON Object
        return jArray;
    }

    public static List<Movies> jsonToMovies(JSONArray jsonArray) {
        List<Movies> listMovies = new ArrayList<Movies>();
        try {
            for (int n = 0; n < jsonArray.length(); n++) {
                JSONObject jsonObject = jsonArray.getJSONObject(n);
                Movies movies = new Movies();
                movies.setId(jsonObject.getInt("id"));
                movies.setTitle(jsonObject.getString("title"));
                movies.setOverview(jsonObject.getString("overview"));
                movies.setMark(jsonObject.getInt("mark"));
                movies.setImageURL(jsonObject.getString("poster_path"));
                JSONArray jsonComments = jsonObject.getJSONArray("comments");
                List<String> list = new ArrayList<String>();
                for (int j = 0; j < jsonComments.length(); j++)
                {
                    JSONObject jobj = jsonComments.getJSONObject(j);
                    list.add(jobj.getString("name") + ": " + jobj.getString("comment"));
                }
                movies.setComments(list);
                listMovies.add(movies);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listMovies;
    }
}