package org.nowxd.popularmovies.activity;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nowxd.popularmovies.R;
import org.nowxd.popularmovies.custom.TrailerAdapter;
import org.nowxd.popularmovies.database.MovieContract;
import org.nowxd.popularmovies.model.Trailer;
import org.nowxd.popularmovies.network.TrailerTaskLoader;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Trailer[]>{

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private TextView movieTitleTextView;
    private ImageView moviePosterImageView;
    private TextView movieRatingTextView;
    private TextView movieReleaseDateTextView;
    private TextView moviePlotTextView;

    private Cursor movieCursor;

    private TrailerAdapter trailerAdapter;

    private int TRAILER_LOADER_ID = 1;
    private int REVIEW_LOADER_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieTitleTextView = (TextView) findViewById(R.id.tv_detail_movie_title);
        moviePosterImageView = (ImageView) findViewById(R.id.iv_detail_movie_poster);
        movieRatingTextView = (TextView) findViewById(R.id.tv_user_rating);
        movieReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        moviePlotTextView = (TextView) findViewById(R.id.tv_movie_plot);

        if (getIntent().hasExtra(getString(R.string.movie_id_intent_key))) {

            long movieID = getIntent().getLongExtra(getString(R.string.movie_id_intent_key), 0);

            movieCursor = initCursor(movieID);

            setUpViews();

        }

        setupRecyclerViews();
        setupLoaders();

    }

    @Override
    protected void onDestroy() {

        if (movieCursor != null) {
            movieCursor.close();
        }

        super.onDestroy();
    }

    private void setupRecyclerViews() {

        RecyclerView trailerRecyclerView = (RecyclerView) findViewById(R.id.rv_trailer_list);

        RecyclerView.LayoutManager trailerLayoutManager = new LinearLayoutManager(this);
        trailerRecyclerView.setLayoutManager(trailerLayoutManager);

        trailerAdapter = new TrailerAdapter();
        trailerRecyclerView.setAdapter(trailerAdapter);

    }

    private void setupLoaders() {

        getSupportLoaderManager().initLoader(TRAILER_LOADER_ID, null, this);

    }

    private Cursor initCursor(long movieID) {

        String whereClause = MovieContract.MovieEntry._ID + "=?";
        String[] whereArgs = {Long.toString(movieID)};

        String[] projection = {
                MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_API_ID,
                MovieContract.MovieEntry.COLUMN_TITLE,
                MovieContract.MovieEntry.COLUMN_IMAGE_URL,
                MovieContract.MovieEntry.COLUMN_PLOT,
                MovieContract.MovieEntry.COLUMN_USER_RATING,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
        };

        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, projection,
                whereClause, whereArgs, null);

        cursor.moveToFirst();

        return cursor;

    }

    private void setUpViews() {

        int titleIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        int imgIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URL);
        int plotIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_PLOT);
        int ratingIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_USER_RATING);
        int releaseDateIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);

        movieTitleTextView.setText(movieCursor.getString(titleIndex));

        Picasso.with(getApplicationContext())
                .load(movieCursor.getString(imgIndex))
                .fit()
                .into(moviePosterImageView);

        String userRating = String.format(
                getResources().getString(R.string.user_rating),
                movieCursor.getDouble(ratingIndex)
        );

        movieRatingTextView.setText(userRating);

        String releaseDate = String.format(
                getResources().getString(R.string.release_date),
                movieCursor.getString(releaseDateIndex)
        );

        movieReleaseDateTextView.setText(releaseDate);

        String moviePlot = String.format(
                getResources().getString(R.string.movie_plot),
                movieCursor.getString(plotIndex)
        );

        moviePlotTextView.setText(moviePlot);

    }

    /**
     * Trailer Loader
     */
    @Override
    public Loader<Trailer[]> onCreateLoader(int id, Bundle args) {

        int idIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_API_ID);
        String movieId = movieCursor.getString(idIndex);

        TrailerTaskLoader loader = new TrailerTaskLoader(this, movieId);
        loader.forceLoad();

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Trailer[]> loader, Trailer[] data) {
        trailerAdapter.setTrailers(data);
    }

    @Override
    public void onLoaderReset(Loader<Trailer[]> loader) {
        trailerAdapter.setTrailers(null);
    }
}
