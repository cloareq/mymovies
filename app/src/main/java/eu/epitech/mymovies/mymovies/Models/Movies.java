package eu.epitech.mymovies.mymovies.Models;

import android.graphics.Bitmap;

/**
 * Created by cloare_q on 4/28/17.
 */

public class Movies {
    String title;
    String overview;
    Bitmap photoId;

    public Movies(String title, String overview, Bitmap photoId) {
        this.title = title;
        this.overview = overview;
        this.photoId = photoId;
    }
}
