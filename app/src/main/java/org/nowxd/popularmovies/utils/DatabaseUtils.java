package org.nowxd.popularmovies.utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.nowxd.popularmovies.data.Movie;
import org.nowxd.popularmovies.database.MovieContract;

public class DatabaseUtils {

    private static String TAG = DatabaseUtils.class.getSimpleName();

    /**
     * Deletes previous movie entries and adds the new movies in the array to the database.
     */
    public synchronized static void updateMovieDatabase(Context context, Movie[] movies, String sortType) {

        // Delete the movies of that sort type first
        String whereClause = MovieContract.MovieEntry.COLUMN_SORT_TYPE + "=?";
        String[] whereArgs = {sortType};
        context.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, whereClause, whereArgs);

        ContentValues[] contentValuesArray = new ContentValues[movies.length];

        for (int index = 0; index < movies.length; index++) {

            contentValuesArray[index] = movies[index].toContentValues(sortType);

        }

        // Bulk insert the new entries
        int insertCount = context.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, contentValuesArray);

        Log.d(TAG, "updateMovieDatabase: NUMBER OF MOVIES ADDED: " + insertCount);

    }

}
