package org.nowxd.popularmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    // Movie Api Fields
    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";

    private static final String MOVIE_VIDEO_PATH = "videos";
    private static final String MOVIE_REVIEW_PATH = "reviews";

    private static final String MOVIE_QUERY_API_KEY = "api_key";

    // Youtube Fields
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/";
    private static final String YOUTUBE_VIDEO_PATH = "watch";
    private static final String YOUTUBE_QUERY_VIDEO_KEY = "v";

    /**
     * Request the list of movies by their sort order (popular or top_rated)
     */
    public static String requestSortedMovies(String sortOrder, String apiKey) {
        return retrieveApiResponse(buildMovieUrl(sortOrder, apiKey));
    }

    private static URL buildMovieUrl(String sortOrder, String apiKey) {

        Uri uri = Uri.parse(MOVIE_BASE_URL)
                .buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(MOVIE_QUERY_API_KEY, apiKey)
                .build();

        return buildUrl(uri);

    }

    /**
     * Trailers
     */
    public static String requestMovieTrailers(String movieApiId, String apiKey) {
        return retrieveApiResponse(buildTrailerUrl(movieApiId, apiKey));
    }

    private static URL buildTrailerUrl(String movieApiId, String apiKey) {

        Uri uri = Uri.parse(MOVIE_BASE_URL)
                .buildUpon()
                .appendPath(movieApiId)
                .appendPath(MOVIE_VIDEO_PATH)
                .appendQueryParameter(MOVIE_QUERY_API_KEY, apiKey)
                .build();

        return buildUrl(uri);

    }

    /**
     * Reviews
     */
    public static String requestMovieReviews(String movieApiId, String apiKey) {
        return retrieveApiResponse(buildReviewUrl(movieApiId, apiKey));
    }

    private static URL buildReviewUrl(String movieApiId, String apiKey) {

        Uri uri = Uri.parse(MOVIE_BASE_URL)
                .buildUpon()
                .appendPath(movieApiId)
                .appendPath(MOVIE_REVIEW_PATH)
                .appendQueryParameter(MOVIE_QUERY_API_KEY, apiKey)
                .build();

        return buildUrl(uri);

    }

    private static URL buildUrl(Uri uri) {

        URL url = null;

        try {

            url = new URL(uri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }

    private static String retrieveApiResponse(URL url) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        String responseBody = null;

        try {

            Response response = client.newCall(request).execute();
            responseBody = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseBody;

    }

    /**
     * Build the Youtube Uri with the video key
     */
    public static Uri buildYoutubeUri(String videoKey) {

        Uri uri = Uri.parse(YOUTUBE_BASE_URL)
                .buildUpon()
                .appendPath(YOUTUBE_VIDEO_PATH)
                .appendQueryParameter(YOUTUBE_QUERY_VIDEO_KEY, videoKey)
                .build();

        return uri;

    }

}
