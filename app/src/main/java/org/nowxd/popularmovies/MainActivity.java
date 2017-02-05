package org.nowxd.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.nowxd.popularmovies.data.Movie;
import org.nowxd.popularmovies.utils.JsonUtils;
import org.nowxd.popularmovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);

        MovieTask movieTask = new MovieTask();
        movieTask.execute("popular");

    }

    /**
     * AsyncTask to fetch movies
     */
    class MovieTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPostExecute(Movie[] movies) {
            movieAdapter.setMovieData(movies);
        }

        @Override
        protected Movie[] doInBackground(String... strings) {

            String sortOrder = strings[0];

            String jsonString = NetworkUtils.requestSortedMovies(sortOrder, getString(R.string.api_key));

            return JsonUtils.processMovieJsonString(jsonString);

        }
    }

}
