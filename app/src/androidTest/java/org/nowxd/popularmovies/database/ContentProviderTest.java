package org.nowxd.popularmovies.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ContentProviderTest {

    private final String TAG = ContentProviderTest.class.getSimpleName();

    private Context context;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        // Wipe the table
        context.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, null, null);
    }

    @Test
    public void testMovieInsert() {

        // Bogus Data
        final String MOVIE_TITLE = "movie_title1";
        final String MOVIE_IMG_URL = "google.com";
        final String MOVIE_PLOT = "hmm";
        final double MOVIE_RATING = 5.5;
        final String MOVIE_RELEASE_DATE = "1/1/2017";
        final String MOVIE_SORT_TYPE = "popular";

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, MOVIE_TITLE);
        contentValues.put(MovieContract.MovieEntry.COLUMN_IMAGE_URL, MOVIE_IMG_URL);
        contentValues.put(MovieContract.MovieEntry.COLUMN_PLOT, MOVIE_PLOT);
        contentValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, MOVIE_RATING);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, MOVIE_RELEASE_DATE);
        contentValues.put(MovieContract.MovieEntry.COLUMN_SORT_TYPE, MOVIE_SORT_TYPE);

        Uri uri = context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        long id = ContentUris.parseId(uri);
        Assert.assertNotEquals(id, -1);

        // Retrieve the Movie
        String[] projection = {
                MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_TITLE,
                MovieContract.MovieEntry.COLUMN_IMAGE_URL,
                MovieContract.MovieEntry.COLUMN_PLOT,
                MovieContract.MovieEntry.COLUMN_USER_RATING,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
        };

        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI, projection, null, null, null);

        // Check the cursor has a count of 1
        Assert.assertNotNull(cursor);
        Assert.assertEquals(cursor.getCount(), 1);
        Assert.assertTrue(cursor.moveToFirst());

        int idIndex = cursor.getColumnIndex(MovieContract.MovieEntry._ID);
        int titleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        int imgIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URL);
        int plotIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_PLOT);
        int ratingIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_USER_RATING);
        int dateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
        int sortIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SORT_TYPE);

        // Assert all the values matches with what we inserted
        Assert.assertEquals(cursor.getLong(idIndex), id);
        Assert.assertEquals(cursor.getString(titleIndex), MOVIE_TITLE);
        Assert.assertEquals(cursor.getString(imgIndex), MOVIE_IMG_URL);
        Assert.assertEquals(cursor.getString(plotIndex), MOVIE_PLOT);
        Assert.assertEquals(cursor.getString(dateIndex), MOVIE_RELEASE_DATE);
        double eps = 1e-10;
        Assert.assertEquals(cursor.getDouble(ratingIndex), MOVIE_RATING, eps);
        Assert.assertEquals(cursor.getString(sortIndex), MOVIE_SORT_TYPE);

        cursor.close();

    }

}
