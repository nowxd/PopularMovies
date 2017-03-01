package org.nowxd.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String TAG = MovieDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "movie.db";
    private static int DATABASE_VERSION = 1;

    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_MOVIE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_API_ID + " TEXT UNIQUE NOT NULL," +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_USER_RATING + " REAL NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL " +
                ");";

        final String CREATE_FAVORITE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + MovieContract.FavoriteEntry.TABLE_NAME + " (" +
                MovieContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                "FOREIGN KEY(" + MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + "(" + MovieContract.MovieEntry._ID + ")" +
                ");";

        final String CREATE_TOP_RATED_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + MovieContract.TopRatedEntry.TABLE_NAME + " (" +
                MovieContract.TopRatedEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.TopRatedEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                "FOREIGN KEY(" + MovieContract.TopRatedEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + "(" + MovieContract.MovieEntry._ID + ")" +
                ");";

        final String CREATE_POPULAR_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + MovieContract.PopularEntry.TABLE_NAME + " (" +
                MovieContract.PopularEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.PopularEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                "FOREIGN KEY(" + MovieContract.PopularEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + "(" + MovieContract.MovieEntry._ID + ")" +
                ");";

        sqLiteDatabase.execSQL(CREATE_MOVIE_TABLE_SQL);
        sqLiteDatabase.execSQL(CREATE_FAVORITE_TABLE_SQL);
        sqLiteDatabase.execSQL(CREATE_TOP_RATED_TABLE_SQL);
        sqLiteDatabase.execSQL(CREATE_POPULAR_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        final String DELETE_MOVIE_TABLE_SQL = "DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME + ";";
        final String DELETE_FAVORITES_TABLE_SQL = "DROP TABLE IF EXISTS " + MovieContract.FavoriteEntry.TABLE_NAME + ";";
        final String DELETE_TOP_RATED_TABLE_SQL = "DROP TABLE IF EXISTS " + MovieContract.TopRatedEntry.TABLE_NAME + ";";
        final String DELETE_POPULAR_TABLE_SQL = "DROP TABLE IF EXISTS " + MovieContract.PopularEntry.TABLE_NAME + ";";

        sqLiteDatabase.execSQL(DELETE_MOVIE_TABLE_SQL);
        sqLiteDatabase.execSQL(DELETE_FAVORITES_TABLE_SQL);
        sqLiteDatabase.execSQL(DELETE_TOP_RATED_TABLE_SQL);
        sqLiteDatabase.execSQL(DELETE_POPULAR_TABLE_SQL);

        onCreate(sqLiteDatabase);

    }

}
