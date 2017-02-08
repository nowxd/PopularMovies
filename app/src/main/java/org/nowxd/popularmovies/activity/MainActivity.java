package org.nowxd.popularmovies.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.nowxd.popularmovies.R;
import org.nowxd.popularmovies.custom.MovieAdapter;
import org.nowxd.popularmovies.data.Movie;
import org.nowxd.popularmovies.utils.GridUtils;
import org.nowxd.popularmovies.utils.JsonUtils;
import org.nowxd.popularmovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MoviePosterOnClickListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private MovieAdapter movieAdapter;

    // Keeps track of the sort by values: popular, top_rated
    private String[] movieSortByValues;
    private int selectionIndex;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Recycler View setup
         */
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        recyclerView.setHasFixedSize(true);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(
                this,
                // Calculate the number of columns needed for the GridView
                GridUtils.calculateNumberOfColumns(getApplicationContext(),
                        getResources().getDimension(R.dimen.movie_poster_width))
        );
        recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(movieAdapter);

        /**
         * Movie sort by values and preferences setup
         */
        movieSortByValues = getResources().getStringArray(R.array.sort_by_array);

        // Using preferences to save the selection index
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        selectionIndex = preferences.getInt(getString(R.string.selection_index_key), 0);

        updateMovies();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        /**
         * Spinner Setup
         */
        MenuItem menuSpinnerItem = menu.findItem(R.id.menu_item_spinner);
        Spinner spinner = (Spinner) menuSpinnerItem.getActionView();

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.sort_by_spinner_item,
                        getResources().getStringArray(R.array.spinner_text_display_array)
                );

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setSelection(selectionIndex, false);

        // Update selection index when spinner item has changed
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                // Update
                selectionIndex = i;
                updateMovies();

                // Save to preferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(getString(R.string.selection_index_key), i);
                editor.apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return true;

    }

    private void updateMovies() {
        new MovieTask().execute(movieSortByValues[selectionIndex]);
    }

    /**
     * On a movie poster click, start the movie detail activity
     */
    @Override
    public void onMoviePosterClick(Movie movie) {

        Class destination = MovieDetailActivity.class;

        Intent intent = new Intent(getApplicationContext(), destination);
        intent.putExtra(getString(R.string.parcelable_movie_key), movie);

        startActivity(intent);

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
