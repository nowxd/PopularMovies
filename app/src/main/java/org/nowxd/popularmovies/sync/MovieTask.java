package org.nowxd.popularmovies.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.nowxd.popularmovies.BuildConfig;
import org.nowxd.popularmovies.data.Movie;
import org.nowxd.popularmovies.utils.DatabaseUtils;
import org.nowxd.popularmovies.utils.JsonUtils;
import org.nowxd.popularmovies.utils.NetworkUtils;

/**
 * AsyncTask to fetch movies
 */
public class MovieTask extends AsyncTask<String, Void, Movie[]> {

    private static final String TAG = MovieTask.class.getSimpleName();

    private Context context;
    private String sortType;

    public MovieTask(Context context) {
        this.context = context;
    }

    @Override
    protected Movie[] doInBackground(String... strings) {

        Log.d(TAG, "doInBackground: STARTING TASK");

        this.sortType = strings[0];

        String jsonString = NetworkUtils.requestSortedMovies(sortType, BuildConfig.MOVIE_API_KEY);

        return JsonUtils.processMovieJsonString(jsonString);

    }

    @Override
    protected void onPostExecute(Movie[] movies) {

        // Update the entries in the database
        DatabaseUtils.updateMovieDatabase(context, movies, sortType);

    }

}
