package eu.epitech.mymovies.mymovies;
 
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import eu.epitech.mymovies.mymovies.Controllers.MovieManager;
import eu.epitech.mymovies.mymovies.Models.Movies;
import eu.epitech.mymovies.mymovies.services.ImgDownloader;

public class MovieActivity extends AppCompatActivity {
    String title;
    String resume;
    String imgurl;
    String userid;
    int id;
    Bitmap img;
    float mark;
    ArrayList<String> comments;
    Button sendCommentButton;
    EditText commentText;
    RatingBar mRatingBar;
    String userComment;
    float rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent intent = getIntent();
        title = intent.getStringExtra("TITLE");
        resume = intent.getStringExtra("RESUME");
        imgurl = intent.getStringExtra("IMGURL");
        id = intent.getIntExtra("ID", 0);
        userid = intent.getStringExtra("USERID");
        mark = intent.getFloatExtra("MARK", 0);
        comments = getIntent().getStringArrayListExtra("COMMENTS");
        sendCommentButton = (Button) findViewById(R.id.send_comment);
        sendCommentButton.setOnClickListener(sendComment);
        commentText   = (EditText)findViewById(R.id.comment);
        mRatingBar = (RatingBar)findViewById(R.id.rating);
        getMovieImg();
        displayMovie();
    }

    View.OnClickListener sendComment = new View.OnClickListener() {
        public void onClick(View v) {
            String comment = commentText.getText().toString();
            if (comment.matches("")) {
                AlertDialog alertDialog = new AlertDialog.Builder(MovieActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Please write a comment");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {
                userComment = commentText.getText().toString();
                rate = (float) (Math.round(mRatingBar.getRating() * 2) / 2.0);
                SendCommentAsync sca = new SendCommentAsync();
                saveMovieInDb();
                sca.execute();
                try {
                    sca.get();
                    finish();
                    Intent intent = getIntent();
                    comments.add(commentText.getText().toString());
                    float userRate = (float) (Math.round(mRatingBar.getRating() * 2) / 2.0);
                    intent.putExtra("MARK", userRate);
                    intent.putStringArrayListExtra("COMMENTS", comments);
                    startActivity(intent);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void saveMovieInDb()
    {
        MovieManager u = new MovieManager(this);
        u.open();
        Movies movie = new Movies(title, resume, imgurl, img, id, userid, mark, comments);
        u.addMovie(movie, Long.parseLong(userid), img);
        u.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                Intent intentHome = new Intent(MovieActivity.this, HomeActivity.class);
                intentHome.putExtra("USERID", userid);
                intentHome.putExtra("USERNAME", "null");
                startActivity(intentHome);
                return true;
            case R.id.logout:
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(MovieActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.usermovies:
                Intent intentUser = new Intent(MovieActivity.this, UserActivity.class);
                intentUser.putExtra("USERID", userid);
                startActivity(intentUser);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startActivity(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.putExtra("UserId", userid);
        }
        super.startActivity(intent);
    }

    private void getMovieImg()
    {
        ImgDownloader imd = new ImgDownloader();
        imd.execute(imgurl);
        try {
            img = imd.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void displayMovie()
    {
        TextView titleTextView = (TextView)findViewById(R.id.title);
        ImageView moviePicture = (ImageView)findViewById(R.id.bigMoviePicture);
        TextView resumeTextView = (TextView)findViewById(R.id.resume);
        ListView listComments = (ListView)findViewById(R.id.listComments);

        moviePicture.setImageBitmap(img);
        titleTextView.setText(title);
        resumeTextView.setText(resume);
        mRatingBar.setRating(mark);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MovieActivity.this,
                android.R.layout.simple_list_item_1, comments);
        listComments.setAdapter(adapter);

    }

    public class SendCommentAsync extends AsyncTask<Void, Void, Void>
    {
        String LOGIN_URL = getResources().getString(R.string.api) + userid + "/" + id;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL(LOGIN_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.connect();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("comment", userComment);
                jsonObject.put("mark", rate);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(jsonObject.toString());
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
                wr.flush();
                wr.close();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void json) {
        }

    }

}
