package eu.epitech.mymovies.mymovies.Models;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Movies {
    String title;
    String overview;
    Bitmap photoId;
    String imageURL;
    String userId;
    float mark;
    int id;
    List<String> comments;

    public Movies(String title, String overview, String imageURL,
                  Bitmap photoId, int id, String userId, float mark, List<String> comments) {
        this.title = title;
        this.overview = overview;
        this.photoId = photoId;
        this.imageURL = imageURL;
        this.id = id;
        this.userId = userId;
        this.mark = mark;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Bitmap getPhotoId() {
        return photoId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getUserId() {
        return userId;
    }

    public float getMark() {
        return mark;
    }

    public List<String> getComments() {
        return comments;
    }
}
