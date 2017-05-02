package eu.epitech.mymovies.mymovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessToken token;
    private String UserName;
    private String UserId;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (LoginButton)findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        if (!isNetworkAvailable()) // pas internet
        {
            System.out.println("PAS INTERNET");
            settings = getSharedPreferences("MYMOVIES", 0);
            UserId = settings.getString("USERID", "null");
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            intent.putExtra("USERID", UserId);
            startActivity(intent);
        }
        else {

            if (AccessToken.getCurrentAccessToken() != null) { // si il est deja connecte en cache
                token = AccessToken.getCurrentAccessToken();
                ConnectToFacebook();
            }


            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    token = loginResult.getAccessToken();
                    String UserToken = loginResult.getAccessToken().getToken();
                    ConnectToFacebook();
                }

                @Override
                public void onCancel() {
                    System.out.println("CANCEL");
                }

                @Override
                public void onError(FacebookException error) {
                    System.out.println(error.toString());
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void ConnectToFacebook(){
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        JSONObject Response = response.getJSONObject();
                        try {
                            UserName = Response.getString("name");
                            UserId = Response.getString("id");

                            //on save le name et l'id en cache pour pouvoir utiliser l'application en offline
                            settings = getSharedPreferences("MYMOVIES", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("USERID", UserId);
                            editor.putString("USERNAME", UserName);
                            editor.commit();

                            //pour rediriger vers la home activity quand on est bien connecté
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra("USERNAME", UserName);
                            intent.putExtra("USERID", UserId);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}


