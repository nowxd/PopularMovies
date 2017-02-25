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

        return uriMatcher;

    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        Uri returnUri;

        switch (match) {

            case MOVIE:

                long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);

                if (id != -1) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert in " + uri);
                }

                break;

            default:

                throw new UnsupportedOperationException("Uri not supported: " + uri);

        }

        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return returnUri;

    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String where, String[] whereArgs, String sortBy) {

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);

        Cursor returnCursor;

        switch (match) {

            case MOVIE:

                returnCursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection, where, whereArgs, null, null, sortBy);
                break;

            default:
                throw new UnsupportedOperationException("Uri not supported: " + uri);

        }

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

//        boolean notifyDelete = true;

        switch (match) {

            case MOVIE:

                deleteCount = db.delete(MovieContract.MovieEntry.TABLE_NAME, where, whereArgs);

//                // Avoid notifying change when deleting movies, since the successive insert will call
//                // notify change
//                notifyDelete = false;

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

                        long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValue);

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
