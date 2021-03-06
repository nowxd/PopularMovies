package org.nowxd.popularmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nowxd.popularmovies.R;
import org.nowxd.popularmovies.custom.ReviewAdapter;
import org.nowxd.popularmovies.custom.TrailerAdapter;
import org.nowxd.popularmovies.database.MovieContract;
import org.nowxd.popularmovies.model.Review;
import org.nowxd.popularmovies.model.Trailer;
import org.nowxd.popularmovies.network.ReviewTaskLoader;
import org.nowxd.popularmovies.network.TrailerTaskLoader;
import org.nowxd.popularmovies.utils.DatabaseUtils;
import org.nowxd.popularmovies.utils.NetworkUtils;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, TrailerAdapter.TrailerOnClickListener {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    @BindView(R.id.tv_detail_movie_title) TextView movieTitleTextView;
    @BindView(R.id.iv_detail_movie_poster) ImageView moviePosterImageView;
    @BindView(R.id.tv_user_rating) TextView movieRatingTextView;
    @BindView(R.id.tv_release_date) TextView movieReleaseDateTextView;
    @BindView(R.id.tv_movie_plot) TextView moviePlotTextView;
    @BindView(R.id.iv_add_to_favorites) ImageView addToFavoritesImageView;

    @BindDrawable(R.drawable.add_to_favorites) Drawable addToFavUnmarked;
    @BindDrawable(R.drawable.add_to_favorites_filled) Drawable addToFavMarked;

    private long movieID;
    private boolean favorited;

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    private final int MOVIE_LOADER_ID = 20;
    private final int TRAILER_LOADER_ID = 21;
    private final int REVIEW_LOADER_ID = 22;

    private Toast favoriteToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        if (getIntent().hasExtra(getString(R.string.movie_id_intent_key))) {

            this.movieID = getIntent().getLongExtra(getString(R.string.movie_id_intent_key), 0);

            setupRecyclerViews();
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);

        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_API_ID,
                MovieContract.MovieEntry.COLUMN_TITLE,
                MovieContract.MovieEntry.COLUMN_IMAGE_URL,
                MovieContract.MovieEntry.COLUMN_PLOT,
                MovieContract.MovieEntry.COLUMN_USER_RATING,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
        };

        String whereClause = MovieContract.MovieEntry._ID + "=?";
        String[] whereArgs = {Long.toString(movieID)};

        return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI, projection, whereClause, whereArgs, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor movieCursor) {

        if (movieCursor != null && movieCursor.moveToFirst()) {

            setupViews(movieCursor);
            setupLoaders(movieCursor);

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void setupViews(Cursor movieCursor) {

        int titleIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        int imgIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URL);
        int plotIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_PLOT);
        int ratingIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_USER_RATING);
        int releaseDateIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);

        movieTitleTextView.setText(movieCursor.getString(titleIndex));

        Picasso.with(getApplicationContext())
                .load(movieCursor.getString(imgIndex))
//                .placeholder(R.drawable.placeholder_image_large)
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

        favorited = DatabaseUtils.checkIfFavorited(this, movieID);

        if (favorited) {
            addToFavoritesImageView.setImageDrawable(addToFavMarked);
        } else {
            addToFavoritesImageView.setImageDrawable(addToFavUnmarked);
        }

        final Context context = this;

        addToFavoritesImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String toastMessage;

                if (favorited) {
                    
                    DatabaseUtils.removeFromFavorites(getApplicationContext(), movieID);
                    addToFavoritesImageView.setImageDrawable(addToFavUnmarked);

                    Log.d(TAG, "onClick: Removed from favorites");
                    toastMessage = "Removed from Favorites";
                    
                } else {

                    DatabaseUtils.addToFavorites(getApplicationContext(), movieID);
                    addToFavoritesImageView.setImageDrawable(addToFavMarked);

                    Log.d(TAG, "onClick: Added to favorites");
                    toastMessage = "Added to Favorites";
                    
                }

                if (favoriteToast != null) favoriteToast.cancel();

                favoriteToast = Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT);
                favoriteToast.show();

                favorited = !favorited;
            }
        });
    }

    private void setupRecyclerViews() {

        // Trailers
        RecyclerView trailerRecyclerView = (RecyclerView) findViewById(R.id.rv_trailer_list);

        RecyclerView.LayoutManager trailerLayoutManager = new LinearLayoutManager(this);
        trailerRecyclerView.setLayoutManager(trailerLayoutManager);

        trailerAdapter = new TrailerAdapter(this);
        trailerRecyclerView.setAdapter(trailerAdapter);

        // Reviews
        RecyclerView reviewRecyclerView = (RecyclerView) findViewById(R.id.rv_review_list);

        RecyclerView.LayoutManager reviewLayoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);

        reviewAdapter = new ReviewAdapter();
        reviewRecyclerView.setAdapter(reviewAdapter);

    }

    /**
     * Loaders for trailers and reviews
     */
    private void setupLoaders(final Cursor movieCursor) {

        final Context context = this;

        // Trailer Loader
        final LoaderManager.LoaderCallbacks<Trailer[]> trailerLoader = new LoaderManager.LoaderCallbacks<Trailer[]>() {
            @Override
            public Loader<Trailer[]> onCreateLoader(int id, Bundle args) {
                int idIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_API_ID);
                String movieId = movieCursor.getString(idIndex);

                TrailerTaskLoader loader = new TrailerTaskLoader(context, movieId);
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
        };

        getSupportLoaderManager().initLoader(TRAILER_LOADER_ID, null, trailerLoader);

        // Review Loader
        final LoaderManager.LoaderCallbacks<Review[]> reviewLoader = new LoaderManager.LoaderCallbacks<Review[]>() {
            @Override
            public Loader<Review[]> onCreateLoader(int id, Bundle args) {

                int idIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_API_ID);
                String movieId = movieCursor.getString(idIndex);

                ReviewTaskLoader loader = new ReviewTaskLoader(context, movieId);
                loader.forceLoad();

                return loader;

            }

            @Override
            public void onLoadFinished(Loader<Review[]> loader, Review[] data) {
                reviewAdapter.setReviews(data);
            }

            @Override
            public void onLoaderReset(Loader<Review[]> loader) {
                reviewAdapter.setReviews(null);
            }
        };

        getSupportLoaderManager().initLoader(REVIEW_LOADER_ID, null, reviewLoader);

    }

    /**
     * On Trailer click, open the youtube video link
     */
    @Override
    public void onTrailerClick(Trailer trailer) {
        startActivity(new Intent(Intent.ACTION_VIEW, NetworkUtils.buildYoutubeUri(trailer.getVideoKey())));
    }
}
