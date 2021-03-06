package org.nowxd.popularmovies.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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

import org.nowxd.popularmovies.R;
import org.nowxd.popularmovies.custom.MovieAdapter;
import org.nowxd.popularmovies.custom.MoviePosterGridLayoutManager;
import org.nowxd.popularmovies.database.MovieContract;
import org.nowxd.popularmovies.network.MovieTask;
import org.nowxd.popularmovies.utils.SyncUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MoviePosterOnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = MainActivity.class.getSimpleName();

    private final int LOADER_ID = 0;

    private MovieAdapter movieAdapter;

    // Keeps track of the movie categories: popular, top_rated, favorites
    private String[] movieCategories;
    private int selectionIndex;

    private SharedPreferences preferences;

    // Limit the number of popular and top rated movies shown to 20
    private static final int MOVIE_LIMIT = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup the RecyclerView
        recyclerViewSetup();

        // Setup the movie category array
        movieCategoriesSetup();

        // Setup sync
        SyncUtils.scheduleMovieSyncJob(this);

        // Loader
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuSpinnerItem = menu.findItem(R.id.menu_item_spinner);
        Spinner spinner = (Spinner) menuSpinnerItem.getActionView();

        spinnerSetup(spinner);

        return true;

    }

    /**
     * RecyclerView Setup
     */
    private void recyclerViewSetup() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new MoviePosterGridLayoutManager(this,
                getResources().getDimension(R.dimen.movie_poster_width));

        recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(movieAdapter);

    }

    /**
     * Movie Categories Setup
     */
    private void movieCategoriesSetup() {

        movieCategories = getResources().getStringArray(R.array.movie_category_array);

        // Using preferences to save the selection index
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        selectionIndex = preferences.getInt(getString(R.string.selection_index_key), 0);

    }

    /**
     * Spinner Setup
     */
    private void spinnerSetup(Spinner spinner) {

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
    }

    // Called when the spinner index has changed
    private void updateSelection(int newSelectionIndex) {
        selectionIndex = newSelectionIndex;
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    /**
     * On a movie poster click, start the movie detail activity
     */
    @Override
    public void onMoviePosterClick(long movieID) {

        Class destination = MovieDetailActivity.class;

        Intent intent = new Intent(getApplicationContext(), destination);
        intent.putExtra(getString(R.string.movie_id_intent_key), movieID);

        startActivity(intent);

    }

    /**
     * CursorLoader methods
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortType = movieCategories[selectionIndex];

        Uri uri = null;
        String orderBy = null;

        if (sortType.equals(getString(R.string.top_rated_sort_value))) {

            uri = MovieContract.TopRatedEntry.CONTENT_URI;
            orderBy = MovieContract.MovieEntry.COLUMN_USER_RATING + " DESC LIMIT " + MOVIE_LIMIT;

        } else if (sortType.equals(getString(R.string.popular_sort_value))) {

            uri = MovieContract.PopularEntry.CONTENT_URI;
            orderBy = MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC LIMIT " + MOVIE_LIMIT;

        } else if (sortType.equals(getString(R.string.favorites_sort_value))) {

            uri = MovieContract.FavoriteEntry.CONTENT_URI;

        }

        return new CursorLoader(this, uri, null, null, null, orderBy);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Manually fetch data if we don't have data to work with
        if (data.getCount() == 0 && !movieCategories[selectionIndex].equals(getString(R.string.favorites_sort_value))) {
            manuallyFetchMovies(movieCategories[selectionIndex]);
        }

        movieAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);
    }

    /**
     * If we do not have data to work with, manually fetch the api instead of waiting for
     * the Firebase job to be dispatched
     */
    public void manuallyFetchMovies(String sortValue) {
        MovieTask movieTask = new MovieTask(this);
        movieTask.execute(sortValue);
    }

}
