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

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

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

    public Movies() {
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPhotoId(Bitmap photoId) {
        this.photoId = photoId;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public void setId(int id) {
        this.id = id;
    }
}
