package eu.epitech.mymovies.mymovies.Models;

/**
 * Created by cloare_q on 4/28/17.
 */

public class Movies {
    String title;
    String overview;
    int photoId;

    public Movies(String title, String overview, int photoId) {
        this.title = title;
        this.overview = overview;
        this.photoId = photoId;
    }
}
