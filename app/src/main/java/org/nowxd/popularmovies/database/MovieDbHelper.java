package org.nowxd.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_MOVIE_TABLE_SQL = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_USER_RATING + " REAL NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_SORT_TYPE + " TEXT NOT NULL" +
                ");";

        sqLiteDatabase.execSQL(CREATE_MOVIE_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        final String DELETE_TABLE_SQL = "DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME;
        sqLiteDatabase.execSQL(DELETE_TABLE_SQL);

        onCreate(sqLiteDatabase);

    }

}
