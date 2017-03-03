package org.nowxd.popularmovies.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.nowxd.popularmovies.model.Movie;
import org.nowxd.popularmovies.database.MovieContract;

public class DatabaseUtils {

    private static String TAG = DatabaseUtils.class.getSimpleName();

    /**
     * Adds the new movies in the array to the database.
     */
    public synchronized static void updateMovieDatabase(Context context, Movie[] movies, String sortType) {

        for (Movie movie : movies) {

            ContentValues contentValues =  movie.toContentValues();

            Uri uri = context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

            long id = ContentUris.parseId(uri);

            if (id == -1) continue;

            // Add to respective sort type tables as well
            if (sortType.equals(MovieContract.TopRatedEntry.TABLE_NAME)) {

                ContentValues joinCV = new ContentValues();
                joinCV.put(MovieContract.TopRatedEntry.COLUMN_MOVIE_ID, id);
                context.getContentResolver().insert(MovieContract.TopRatedEntry.CONTENT_URI, joinCV);

            } else if (sortType.equals(MovieContract.PopularEntry.TABLE_NAME)) {

                ContentValues joinCV = new ContentValues();
                joinCV.put(MovieContract.PopularEntry.COLUMN_MOVIE_ID, id);
                context.getContentResolver().insert(MovieContract.PopularEntry.CONTENT_URI, joinCV);

            }

        }

        Log.d(TAG, "updateMovieDatabase: " + sortType + " movies updated!");

    }

    /**
     * Check if the movie is in the favorites table
     */
    public static boolean checkIfFavorited(Context context, long movieId) {

        String whereClause = MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + "=?";
        String[] whereArgs = {Long.toString(movieId)};

        Cursor cursor = context.getContentResolver().query(MovieContract.FavoriteEntry.CONTENT_URI, null, whereClause, whereArgs,
                null);

        boolean result = cursor != null && cursor.getCount() > 0;

        if (cursor != null) cursor.close();

        return result;

    }

    /**
     * Add the movie id to the favorites table
     */
    public static void addToFavorites(Context context, long movieId) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID, movieId);

        context.getContentResolver().insert(MovieContract.FavoriteEntry.CONTENT_URI, contentValues);

    }

    /**
     * Remove the movie id from the favorites table
     */
    public static void removeFromFavorites(Context context, long movieId) {

        String whereClause = MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + "=?";
        String[] whereArgs = {Long.toString(movieId)};

        context.getContentResolver().delete(MovieContract.FavoriteEntry.CONTENT_URI, whereClause, whereArgs);

    }

}
