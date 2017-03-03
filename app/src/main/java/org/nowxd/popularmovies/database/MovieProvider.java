package org.nowxd.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class MovieProvider extends ContentProvider {

    public static final int MOVIE = 100;
    public static final int FAVORITE = 200;
    public static final int TOP_RATED = 300;
    public static final int POPULAR = 400;

    private static UriMatcher uriMatcher = buildUriMatcher();

    private MovieDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return true;
    }

    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.MOVIE_PATH, MOVIE);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.FAVORITE_PATH, FAVORITE);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.TOP_RATED_PATH, TOP_RATED);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.POPULAR_PATH, POPULAR);

        return uriMatcher;

    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        long id;
        Uri returnUri;

        switch (match) {

            case MOVIE:

                id = db.insertWithOnConflict(MovieContract.MovieEntry.TABLE_NAME, null, contentValues,
                        SQLiteDatabase.CONFLICT_IGNORE);
                break;

            case FAVORITE:

                id = db.insertWithOnConflict(MovieContract.FavoriteEntry.TABLE_NAME, null, contentValues,
                        SQLiteDatabase.CONFLICT_IGNORE);
                break;

            case TOP_RATED:

                id = db.insertWithOnConflict(MovieContract.TopRatedEntry.TABLE_NAME, null, contentValues,
                        SQLiteDatabase.CONFLICT_IGNORE);
                break;

            case POPULAR:

                id = db.insertWithOnConflict(MovieContract.PopularEntry.TABLE_NAME, null, contentValues,
                        SQLiteDatabase.CONFLICT_IGNORE);
                break;

            default:
                throw new UnsupportedOperationException("Uri not supported: " + uri);
        }

        returnUri = ContentUris.withAppendedId(uri, id);

        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return returnUri;

    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String where, String[] whereArgs, String orderBy) {

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);

        String table;

        switch (match) {

            case MOVIE:

                table = MovieContract.MovieEntry.TABLE_NAME;

                break;

            case FAVORITE:

                table = MovieContract.FavoriteEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME + " ON " +
                        MovieContract.FavoriteEntry.TABLE_NAME + "." + MovieContract.FavoriteEntry.COLUMN_MOVIE_ID +
                        " = " +
                        MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID;

                break;

            case TOP_RATED:

                table = MovieContract.TopRatedEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME + " ON " +
                        MovieContract.TopRatedEntry.TABLE_NAME + "." + MovieContract.TopRatedEntry.COLUMN_MOVIE_ID +
                        " = " +
                        MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID;

                break;

            case POPULAR:

                table = MovieContract.PopularEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME + " ON " +
                        MovieContract.PopularEntry.TABLE_NAME + "." + MovieContract.PopularEntry.COLUMN_MOVIE_ID +
                        " = " +
                        MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID;

                break;

            default:
                throw new UnsupportedOperationException("Uri not supported: " + uri);

        }

        Cursor returnCursor = db.query(table, projection, where, whereArgs, null, null, orderBy);

        if (getContext() != null) {
            returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return returnCursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String where, String[] whereArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        int deleteCount;

        switch (match) {

            case MOVIE:

                deleteCount = db.delete(MovieContract.MovieEntry.TABLE_NAME, where, whereArgs);
                break;

            case FAVORITE:

                deleteCount = db.delete(MovieContract.FavoriteEntry.TABLE_NAME, where, whereArgs);
                break;

            default:
                throw new UnsupportedOperationException("Uri not supported: " + uri);

        }

        if (getContext() != null && deleteCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String where, String[] whereArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        int updateCount;

        switch (match) {

            case MOVIE:

                updateCount = db.update(MovieContract.MovieEntry.TABLE_NAME, contentValues, where, whereArgs);
                break;

            default:
                throw new UnsupportedOperationException("Uri not supported: " + uri);

        }

        if (getContext() != null && updateCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updateCount;

    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] contentValues) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        int insertCount = 0;

        switch (match) {

            case MOVIE:

                db.beginTransaction();

                try {

                    for (ContentValues contentValue : contentValues) {

                        long id = db.replace(MovieContract.MovieEntry.TABLE_NAME, null, contentValue);

                        if (id != -1) {
                            insertCount++;
                        }

                    }

                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();
                }

                break;

            default:
                insertCount = super.bulkInsert(uri, contentValues);
                break;

        }

        if (getContext() != null && insertCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return insertCount;

    }

}
