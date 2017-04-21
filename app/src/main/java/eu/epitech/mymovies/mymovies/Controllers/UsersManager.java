package eu.epitech.mymovies.mymovies.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import eu.epitech.mymovies.mymovies.Models.Users;
import eu.epitech.mymovies.mymovies.services.MySQLite;

public class UsersManager {

    private static final String TABLE_NAME = "USERS";
    public static final String KEY_ID_Users="id";
    public static final String KEY_NOM_Users="name";
    public static final String CREATE_TABLE_Users = "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+KEY_ID_Users+" INTEGER primary key," +
            " "+KEY_NOM_Users+" TEXT" +
            ");";
    private MySQLite maBaseSQLite;
    private SQLiteDatabase db;

    public UsersManager(Context context)
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
        db.execSQL(CREATE_TABLE_Users);
    }

    public long addUsers(Users user) {
        ContentValues values = new ContentValues();
        values.put(KEY_NOM_Users, user.getName());
        values.put(KEY_ID_Users, user.getId());

        return db.insert(TABLE_NAME,null,values);
    }

    public int modUsers(Users user) {
        ContentValues values = new ContentValues();
        values.put(KEY_NOM_Users, user.getName());

        String where = KEY_ID_Users+" = ?";
        String[] whereArgs = {user.getId()+""};

        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    public int supUsers(Users user) {
        String where = KEY_ID_Users+" = ?";
        String[] whereArgs = {user.getId()+""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public Users getUsers(long id) {
        Users u=new Users(0,"");

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_ID_Users+"="+id, null);
        if (c.moveToFirst()) {
            u.setId(c.getInt(c.getColumnIndex(KEY_ID_Users)));
            u.setName(c.getString(c.getColumnIndex(KEY_NOM_Users)));
            c.close();
        }

        return u;
    }

    public Cursor getUsers() {
        return db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
    }

}
