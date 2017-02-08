package org.nowxd.popularmovies.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    private int spinnerIndex;

    // Keeps track of the sort by values: popular, top_rated
    private String[] sortByValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        recyclerView.setHasFixedSize(true);

        // Retrieve the height to calculate the number of columns
        float heightPx = getResources().getDimension(R.dimen.movie_poster_height);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(
                this,
                GridUtils.calculateNumberOfColumns(getApplicationContext(), heightPx)
        );
        recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(movieAdapter);

        if (savedInstanceState == null) {
            spinnerIndex = 0;
        } else {
            spinnerIndex = savedInstanceState.getInt(getString(R.string.spinner_key));
        }

        sortByValues = getResources().getStringArray(R.array.sort_by_array);

        updateMovies();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuSpinnerItem = menu.findItem(R.id.menu_item_spinner);
        Spinner spinner = (Spinner) menuSpinnerItem.getActionView();

        // Update the spinner index and movies shown
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerIndex = i;
                updateMovies();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.sort_by_spinner_item,
                        getResources().getStringArray(R.array.spinner_text_display_array)
                );

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setSelection(spinnerIndex, false);

        return true;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.spinner_key), spinnerIndex);
    }

    private void updateMovies() {
        new MovieTask().execute(sortByValues[spinnerIndex]);
    }

    /**
     * On movie poster click, start the movie detail activity
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
