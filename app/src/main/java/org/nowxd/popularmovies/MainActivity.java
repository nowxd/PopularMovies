package org.nowxd.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.nowxd.popularmovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovieTask movieTask = new MovieTask();
        movieTask.execute("popular");

    }

    class MovieTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String jsonBody) {

            if (jsonBody != null) {
                Log.d(TAG, "onPostExecute: " + jsonBody);
            }

        }

        @Override
        protected String doInBackground(String... strings) {

            String sortOrder = strings[0];

            return NetworkUtils.requestSortedMovies(sortOrder, getString(R.string.api_key));

        }
    }

}
