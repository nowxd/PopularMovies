package org.nowxd.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.nowxd.popularmovies.data.Movie;
import org.nowxd.popularmovies.utils.JsonUtils;
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

    /**
     * AsyncTask to fetch movies
     */
    class MovieTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPostExecute(Movie[] movies) {

            if (movies == null) return;

            for (Movie movie : movies) {

                Log.d(TAG, "onPostExecute: " + movie);

            }

        }

        @Override
        protected Movie[] doInBackground(String... strings) {

            String sortOrder = strings[0];

            String jsonString = NetworkUtils.requestSortedMovies(sortOrder, getString(R.string.api_key));

            return JsonUtils.processMovieJsonString(jsonString);

        }
    }

}
