package org.nowxd.popularmovies.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;

import org.nowxd.popularmovies.R;
import org.nowxd.popularmovies.custom.MovieAdapter;
import org.nowxd.popularmovies.custom.MoviePosterGridLayoutManager;
import org.nowxd.popularmovies.data.Movie;
import org.nowxd.popularmovies.database.MovieContract;
import org.nowxd.popularmovies.sync.MovieFireBaseJobService;
import org.nowxd.popularmovies.sync.MovieTask;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MoviePosterOnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = MainActivity.class.getSimpleName();

    private final int LOADER_ID = 0;

    private MovieAdapter movieAdapter;

    // Keeps track of the sort-by values: popular, top_rated
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

        RecyclerView.LayoutManager layoutManager = new MoviePosterGridLayoutManager(this,
                getResources().getDimension(R.dimen.movie_poster_width),
                getResources().getDimension(R.dimen.movie_poster_height));

        recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(movieAdapter);

        /**
         * Movie sort-by values and preferences setup
         */
        movieSortByValues = getResources().getStringArray(R.array.sort_by_array);

        // Using preferences to save the selection index
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        selectionIndex = preferences.getInt(getString(R.string.selection_index_key), 0);

        initFireBaseDispatcher();

        // Loader
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

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
                updateSelection(i);

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

    // Called when the spinner index has changed
    private void updateSelection(int newSelectionIndex) {

        selectionIndex = newSelectionIndex;
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);

    }

    /**
     * TODO Dispatcher not working atm, fix it so we don't have to manually execute the updates
     */
    private void initFireBaseDispatcher() {

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

        Job syncMoviesJob = dispatcher.newJobBuilder()
                .setService(MovieFireBaseJobService.class)
                .setTag("moviesApiCall")
                .build();

        dispatcher.schedule(syncMoviesJob);

        MovieTask movieTask1 = new MovieTask(getApplicationContext());
        MovieTask movieTask2 = new MovieTask(getApplicationContext());

        movieTask1.execute("popular");
        movieTask2.execute("top_rated");

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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortType = movieSortByValues[selectionIndex];

        String whereClause = MovieContract.MovieEntry.COLUMN_SORT_TYPE + "=?";
        String[] whereArgs = {sortType};

//        String sortBy =

        return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI, null, whereClause,
                whereArgs, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        movieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);
    }
}
