package org.nowxd.popularmovies.model;

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

    private String apiId;
    private String title;
    private String posterImageUrl;
    private String plotSynopsis;
    private String releaseDate;
    private double userRating;
    private double popularity;

    // JSON keys
    private static final String API_ID_KEY = "id";
    private static final String TITLE_KEY = "title";
    private static final String POSTER_KEY = "poster_path";
    private static final String PLOT_KEY = "overview";
    private static final String RELEASE_DATE_KEY = "release_date";
    private static final String USER_RATING_KEY = "vote_average";
    private static final String POPULARITY_KEY = "popularity";

    public Movie(JSONObject jsonObject) {

        try {

            apiId = jsonObject.getString(API_ID_KEY);
            title = jsonObject.getString(TITLE_KEY);
            posterImageUrl = "http://image.tmdb.org/t/p/w185" + jsonObject.getString(POSTER_KEY);
            plotSynopsis = jsonObject.getString(PLOT_KEY);
            releaseDate = jsonObject.getString(RELEASE_DATE_KEY);
            userRating = jsonObject.getDouble(USER_RATING_KEY);
            popularity = jsonObject.getDouble(POPULARITY_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ContentValues toContentValues() {

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.MovieEntry.COLUMN_API_ID, this.apiId);
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, this.title);
        contentValues.put(MovieContract.MovieEntry.COLUMN_IMAGE_URL, this.getPosterImageUrl());
        contentValues.put(MovieContract.MovieEntry.COLUMN_PLOT, this.getPlotSynopsis());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, this.getReleaseDate());
        contentValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, this.getUserRating());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, this.popularity);
        contentValues.put(MovieContract.MovieEntry.COLUMN_CURRENT, 1);

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

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "apiId='" + apiId + '\'' +
                ", title='" + title + '\'' +
                ", posterImageUrl='" + posterImageUrl + '\'' +
                ", plotSynopsis='" + plotSynopsis + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", userRating=" + userRating +
                ", popularity=" + popularity +
                '}';
    }

    protected Movie(Parcel in) {
        apiId = in.readString();
        title = in.readString();
        posterImageUrl = in.readString();
        plotSynopsis = in.readString();
        releaseDate = in.readString();
        userRating = in.readDouble();
        popularity = in.readDouble();
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
        parcel.writeString(apiId);
        parcel.writeString(title);
        parcel.writeString(posterImageUrl);
        parcel.writeString(plotSynopsis);
        parcel.writeString(releaseDate);
        parcel.writeDouble(userRating);
        parcel.writeDouble(popularity);
    }
}
