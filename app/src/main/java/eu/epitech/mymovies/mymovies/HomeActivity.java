package eu.epitech.mymovies.mymovies;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import eu.epitech.mymovies.mymovies.Controllers.UsersManager;
import eu.epitech.mymovies.mymovies.Models.Movies;
import eu.epitech.mymovies.mymovies.Models.Users;
import eu.epitech.mymovies.mymovies.services.GetAsync2;
import eu.epitech.mymovies.mymovies.services.ImageAdapter;
import eu.epitech.mymovies.mymovies.services.JSONParser;
import eu.epitech.mymovies.mymovies.services.PostAsync;

public class HomeActivity extends AppCompatActivity {

    private String UserName;
    private String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        UserName = intent.getStringExtra("USERNAME");
        UserId = intent.getStringExtra("USERID");
        logAllUsersFromDB();
        if (UserId != "0")
        {
            Log.d("L'ID", UserId);
            if (!userIsInDb())
                putUserInDB();
            registerUserInExternalDB();
        }
        putMovieInHomePage();
    }

    private void putMovieInHomePage()
    {
        GetAsync2 get = new GetAsync2();
        get.execute(getResources().getString(R.string.api) + "discover/10212801988222217/1");
        List<Movies> listMovies = new ArrayList<Movies>();
        try {
            listMovies = JSONParser.jsonToMovies(get.get());
            String response = get.get().toString();
            System.out.println(response);
            System.out.println(listMovies);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, listMovies));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(HomeActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void startActivity(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.putExtra("UserId", UserId);
        }
        super.startActivity(intent);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            case R.id.usermovies:
                Intent intentUser = new Intent(HomeActivity.this, UserActivity.class);
                intentUser.putExtra("USERID", UserId);
                startActivity(intentUser);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean userIsInDb()
    {
        UsersManager u = new UsersManager(this);
        Users user;
        u.open();
        user = u.getUsers(Long.parseLong(UserId));
        if (user.getName() != "") {
            return true;
        }
        return false;
    }

    private void putUserInDB()
    {
        UsersManager u = new UsersManager(this);
        u.open();
        u.addUsers(new Users(Long.parseLong(UserId),UserName));
        u.close();
    }

    private void registerUserInExternalDB()
    {
        PostAsync post = new PostAsync();
        post.execute(getResources().getString(R.string.loginurl), UserId, UserName);
        try {
            String response = post.get().toString();
            System.out.println(response);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void logAllUsersFromDB()
    {
        UsersManager u = new UsersManager(this);
        u.open();

        Cursor c = u.getUsers();
        if (c.moveToFirst())
        {
            do {
                Log.d("Users :",
                        c.getLong(c.getColumnIndex(UsersManager.KEY_ID_Users)) + "," +
                                c.getString(c.getColumnIndex(UsersManager.KEY_NOM_Users))
                );
            }
            while (c.moveToNext());
        }
        c.close();
        u.close();
    }
}
