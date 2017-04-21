package eu.epitech.mymovies.mymovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
        Log.d("L'ID", UserId);
        putUserInDB();
    }

    private void putUserInDB()
    {
        UsersManager u = new UsersManager(this);
        u.open();
        u.addUsers(new Users(Long.parseLong(UserId),UserName));
        u.close();
    }

    private void logAllUsersFromDB() //je l'ai fait pour tester mais je pense pas qu'on en aura besoin
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
