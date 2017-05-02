package eu.epitech.mymovies.mymovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import eu.epitech.mymovies.mymovies.Controllers.MovieManager;
import eu.epitech.mymovies.mymovies.Models.Movies;
import eu.epitech.mymovies.mymovies.Models.RVAdapter;
import eu.epitech.mymovies.mymovies.Models.RVAdapterOffline;

public class UserActivity extends AppCompatActivity {

    String userId;
    List<Movies> listMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();
        userId = intent.getStringExtra("USERID");
        getUserMovies();
    }

    private void getUserMovies() {
        MovieManager movieManager = new MovieManager(this);
        movieManager.open();
        listMovies = movieManager.getUserMovies(Long.parseLong(userId));
        movieManager.close();
        createRecyclerView();
    }

    private void createRecyclerView()
    {
        setContentView(R.layout.activity_search);
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapterOffline adapter = new RVAdapterOffline(listMovies, this);
        rv.setAdapter(adapter);
    }
}
