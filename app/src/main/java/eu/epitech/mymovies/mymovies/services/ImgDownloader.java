package eu.epitech.mymovies.mymovies.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImgDownloader extends AsyncTask<String, Void, Bitmap> {

    /** progress dialog to show user that the backup is processing. */
    /** application context. */
    @Override
    protected void onPreExecute() {
    }



    protected Bitmap doInBackground(String... param) {
        try {
            URL url = new URL(param[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {System.out.println("Error :: [" + e + "]");}
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bp) {
    }
}
