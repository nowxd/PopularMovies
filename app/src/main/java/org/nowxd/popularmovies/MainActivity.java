package org.nowxd.popularmovies;

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

import org.nowxd.popularmovies.data.Movie;
import org.nowxd.popularmovies.utils.JsonUtils;
import org.nowxd.popularmovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private RecyclerView.LayoutManager layoutManager;

    /**
     * Spinner Vars
     */
    private int spinnerIndex;
    private static final String SPINNER_KEY = "spinner_key";
    private static final String[] SPINNER_TEXT_DISPLAY = {"Popular", "Top Rated"};
    private static final String[] SPINNER_VALUES = {"popular", "top_rated"};

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

        if (savedInstanceState == null) {
            spinnerIndex = 0;
        } else {
            spinnerIndex = savedInstanceState.getInt(SPINNER_KEY);
        }

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
                new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, SPINNER_TEXT_DISPLAY);

        spinner.setAdapter(adapter);

        spinner.setSelection(spinnerIndex, false);

        return true;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SPINNER_KEY, spinnerIndex);
    }

    private void updateMovies() {
        new MovieTask().execute(SPINNER_VALUES[spinnerIndex]);
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
