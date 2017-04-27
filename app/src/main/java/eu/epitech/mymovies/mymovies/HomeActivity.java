package eu.epitech.mymovies.mymovies;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.login.LoginManager;

import eu.epitech.mymovies.mymovies.Controllers.UsersManager;
import eu.epitech.mymovies.mymovies.Models.Users;

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
        if (UserId != "0")
        {
            Log.d("L'ID", UserId);
            putUserInDB();
        }
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
    public boolean onOptionsItemSelected(MenuItem item) { //ca c'est pour le logout, je sais pas si y'a une meilleur facon de le faire
        switch (item.getItemId()){
            case R.id.logout:
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void putUserInDB()
    {
        UsersManager u = new UsersManager(this);
        u.open();
        u.addUsers(new Users(Long.parseLong(UserId),UserName));
        u.close();
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
