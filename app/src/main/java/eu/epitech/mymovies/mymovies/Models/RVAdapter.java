package eu.epitech.mymovies.mymovies.Models;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import eu.epitech.mymovies.mymovies.HomeActivity;
import eu.epitech.mymovies.mymovies.MovieActivity;
import eu.epitech.mymovies.mymovies.R;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MovieViewHolder>{

    List<Movies> movies;

    Context context;

    public RVAdapter(List<Movies> movies, Context context){
        this.movies=movies;
        this.context=context;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_movies, viewGroup, false);
        MovieViewHolder mvh = new MovieViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder movieViewHolder, final int i) {
        movieViewHolder.movieTitle.setText(movies.get(i).title);
        movieViewHolder.movieResume.setText(movies.get(i).overview);
        movieViewHolder.moviePicture.setImageBitmap(movies.get(i).photoId);

        movieViewHolder.moviePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(movies.get(i).title);
                Intent intent = new Intent(context, MovieActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView movieTitle;
        TextView movieResume;
        ImageView moviePicture;

        MovieViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            movieTitle = (TextView)itemView.findViewById(R.id.movie_title);
            movieResume = (TextView)itemView.findViewById(R.id.movie_resume);
            moviePicture = (ImageView)itemView.findViewById(R.id.movie_picture);
        }
    }

}