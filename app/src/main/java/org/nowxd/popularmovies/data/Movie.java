package org.nowxd.popularmovies.data;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;
import org.nowxd.popularmovies.database.MovieContract;

public class Movie implements Parcelable {

    /**
        original title
        movie poster image thumbnail
        A plot synopsis (called overview in the api)
        user rating (called vote_average in the api)
        release date
     */

    // TODO Retrieve Movie API ID and popularity (sort by) as well
    private String title;
    private String posterImageUrl;
    private String plotSynopsis;
    private double userRating;
    private String releaseDate;
//    private String apiId;

    // JSON keys
    private static final String TITLE_KEY = "title";
    private static final String POSTER_KEY = "poster_path";
    private static final String PLOT_KEY = "overview";
    private static final String USER_RATING_KEY = "vote_average";
    private static final String RELEASE_DATE_KEY = "release_date";

    public Movie(JSONObject jsonObject) {

        try {

            title = jsonObject.getString(TITLE_KEY);
            posterImageUrl = "http://image.tmdb.org/t/p/w185" + jsonObject.getString(POSTER_KEY);
            plotSynopsis = jsonObject.getString(PLOT_KEY);
            userRating = jsonObject.getDouble(USER_RATING_KEY);
            releaseDate = jsonObject.getString(RELEASE_DATE_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ContentValues toContentValues(String sortType) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, this.title);
        contentValues.put(MovieContract.MovieEntry.COLUMN_IMAGE_URL, this.getPosterImageUrl());
        contentValues.put(MovieContract.MovieEntry.COLUMN_PLOT, this.getPlotSynopsis());
        contentValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, this.getUserRating());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, this.getReleaseDate());
        contentValues.put(MovieContract.MovieEntry.COLUMN_SORT_TYPE, sortType);

        return contentValues;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterImageUrl() {
        return posterImageUrl;
    }

    public void setPosterImageUrl(String posterImageUrl) {
        this.posterImageUrl = posterImageUrl;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", posterImageUrl='" + posterImageUrl + '\'' +
                ", plotSynopsis='" + plotSynopsis + '\'' +
                ", userRating=" + userRating +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    protected Movie(Parcel in) {
        title = in.readString();
        posterImageUrl = in.readString();
        plotSynopsis = in.readString();
        userRating = in.readDouble();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(posterImageUrl);
        parcel.writeString(plotSynopsis);
        parcel.writeDouble(userRating);
        parcel.writeString(releaseDate);
    }

}
