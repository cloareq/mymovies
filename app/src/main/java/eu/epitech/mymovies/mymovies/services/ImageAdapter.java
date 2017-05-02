package eu.epitech.mymovies.mymovies.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import eu.epitech.mymovies.mymovies.Models.Movies;
import eu.epitech.mymovies.mymovies.R;

/**
 * Created by Aurélien on 02/05/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Movies> listMovies;
    private Bitmap[] mThumbIds;

    public ImageAdapter(Context c, List<Movies> listMovies) {
        Bitmap[] mThumbIds = {
                getMovieImg(listMovies.get(0).getImageURL()), getMovieImg(listMovies.get(1).getImageURL()),
                getMovieImg(listMovies.get(2).getImageURL()), getMovieImg(listMovies.get(3).getImageURL()),
                getMovieImg(listMovies.get(4).getImageURL()), getMovieImg(listMovies.get(5).getImageURL()),
                getMovieImg(listMovies.get(6).getImageURL()), getMovieImg(listMovies.get(7).getImageURL()),
                getMovieImg(listMovies.get(8).getImageURL()), getMovieImg(listMovies.get(9).getImageURL()),
                getMovieImg(listMovies.get(10).getImageURL()), getMovieImg(listMovies.get(11).getImageURL()),
                getMovieImg(listMovies.get(12).getImageURL()), getMovieImg(listMovies.get(13).getImageURL()),
                getMovieImg(listMovies.get(14).getImageURL()), getMovieImg(listMovies.get(15).getImageURL()),
                getMovieImg(listMovies.get(16).getImageURL()), getMovieImg(listMovies.get(17).getImageURL()),
                getMovieImg(listMovies.get(18).getImageURL()), getMovieImg(listMovies.get(19).getImageURL())
        };
        mContext = c;
        this.mThumbIds = mThumbIds;
        this.listMovies = listMovies;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(400, 600));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(mThumbIds[position]);
        return imageView;
    }

    private Bitmap getMovieImg(String imgurl)
    {
        ImgDownloader imd = new ImgDownloader();
        imd.execute(imgurl);
        Bitmap img = null;
        try {
            img = imd.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return img;
    }
}