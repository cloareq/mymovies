package eu.epitech.mymovies.mymovies.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.epitech.mymovies.mymovies.Models.Movies;
import eu.epitech.mymovies.mymovies.Models.Users;
import eu.epitech.mymovies.mymovies.services.ByteToData;
import eu.epitech.mymovies.mymovies.services.MySQLite;

public class MovieManager {

    private static final String TABLE_NAME = "Movies";
    public static final String KEY_ID_MOVIE= "id";
    public static final String KEY_TITLE= "title";
    public static final String KEY_RESUME = "resume";
    public static final String KEY_RATE = "rate";
    public static final String KEY_COMMENT = "comments";
    public static final String KEY_PICTURE = "picture";
    public static final String KEY_USERID = "userid";

    public static final String CREATE_TABLE_MOVIES = "create table "
            + TABLE_NAME + " ("
            + KEY_ID_MOVIE + " integer primary key, "
            + KEY_TITLE + " text not null, "
            + KEY_RESUME + " text not null, "
            + KEY_RATE + " integer,"
            + KEY_COMMENT + " text,"
            + KEY_PICTURE + " BLOB,"
            + KEY_USERID + " integer,"
            + " FOREIGN KEY ("+ KEY_USERID +") REFERENCES "+ "USERS" +"(id));";

    private MySQLite maBaseSQLite;
    private SQLiteDatabase db;

    public MovieManager(Context context)
    {
        maBaseSQLite = MySQLite.getInstance(context);
    }

    public void open()
    {
        db = maBaseSQLite.getWritableDatabase();
    }

    public void close()
    {
        db.close();
    }

    public void deleteTable()
    {
        db.execSQL("DROP TABLE IF EXISTS USERS");
        db.execSQL(CREATE_TABLE_MOVIES);
    }

    public long addMovie(Movies movie, long userid, Bitmap img) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID_MOVIE, movie.getId());
        values.put(KEY_TITLE, movie.getTitle());
        values.put(KEY_RESUME, movie.getOverview());
        values.put(KEY_RATE, movie.getMark());
        values.put(KEY_COMMENT, TextUtils.join(",", movie.getComments()));
        values.put(KEY_PICTURE, ByteToData.getBytes(img));
        values.put(KEY_USERID, userid);

        return db.insert(TABLE_NAME,null,values);
    }

    public List<Movies> getUserMovies(long id) {
        List<Movies> listMovies = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_USERID+"="+id, null);
        if (c.moveToFirst()) {
            do {
                Movies m =new Movies();
                m.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                m.setOverview(c.getString(c.getColumnIndex(KEY_RESUME)));
                m.setPhotoId(ByteToData.getImage(c.getBlob(c.getColumnIndex(KEY_PICTURE))));
                m.setUserId(c.getString(c.getColumnIndex(KEY_USERID)));
                m.setMark(c.getFloat(c.getColumnIndex(KEY_RATE)));
                String[] comments = c.getString(c.getColumnIndex(KEY_COMMENT)).split(",");
                m.setComments(Arrays.asList(comments));
                listMovies.add(m);
            } while (c.moveToNext());
        }
        return listMovies;
    }



    public Cursor getUsers() {
        return db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
    }

}
