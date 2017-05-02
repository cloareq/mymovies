package eu.epitech.mymovies.mymovies;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import eu.epitech.mymovies.mymovies.Models.Movies;
import eu.epitech.mymovies.mymovies.Models.RVAdapter;
import eu.epitech.mymovies.mymovies.services.ImgDownloader;
import eu.epitech.mymovies.mymovies.services.JSONParser;

public class SearchActivity extends AppCompatActivity {
    private String UserId;
    private ArrayList movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        Intent intent = getIntent();
        UserId = intent.getStringExtra("UserId");
        setContentView(R.layout.activity_search);
        createRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public void startActivity(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.putExtra("UserId", UserId);
        }
        super.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void createRecyclerView()
    {
        setContentView(R.layout.activity_search);
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapter adapter = new RVAdapter(movies, this);
        rv.setAdapter(adapter);
    }

    private void setMoviesList(JSONArray response)
    {

        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject arr = response.getJSONObject(i);
                ImgDownloader imd = new ImgDownloader();
                imd.execute(arr.getString("poster_path"));
                Bitmap movie_image = imd.get();

                JSONArray jarr = arr.getJSONArray("comments");
                List<String> list = new ArrayList<String>();
                for (int j = 0; j < jarr.length(); j++)
                {
                    JSONObject jobj = jarr.getJSONObject(j);
                    list.add(jobj.getString("comment"));
                }

                movies.add(new Movies(arr.getString("title"), arr.getString("overview"),
                        arr.getString("poster_path"), movie_image, arr.getInt("id"), UserId,
                        arr.getInt("averageMark"), list));
            } catch (JSONException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        createRecyclerView();
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            UserId = intent.getStringExtra("UserId");

            SearchMoviesAsync search = new SearchMoviesAsync();
            search.execute(UserId, query);
        }
    }

    public class SearchMoviesAsync extends AsyncTask<String, String, JSONArray> {
        ProgressDialog pd;
        JSONParser jsonParser = new JSONParser();
        String URL = getResources().getString(R.string.api) + "search";


        @Override
        protected void onPreExecute() {
            pd=ProgressDialog.show(SearchActivity.this,"","Please Wait",false);
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

        @Override
        protected void onPostExecute(JSONArray response) {
            setMoviesList(response);
            pd.dismiss();
        }

    }
}
