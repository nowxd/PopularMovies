package org.nowxd.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nowxd.popularmovies.model.Movie;
import org.nowxd.popularmovies.model.Review;
import org.nowxd.popularmovies.model.Trailer;

import java.util.ArrayList;

public class JsonUtils {

    private static final String JSON_ARRAY_KEY = "results";

    /**
     * Convert the JSON string into an array of movies
     */
    public static Movie[] processMovieJsonString(String jsonData) {

        if (jsonData == null) return null;

        Movie[] movies = null;

        try {

            JSONArray jsonArray = new JSONObject(jsonData).getJSONArray(JSON_ARRAY_KEY);

            movies = new Movie[jsonArray.length()];

            for (int index = 0; index < jsonArray.length(); index++) {

                movies[index] = new Movie(jsonArray.getJSONObject(index));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;

    }

    public static Trailer[] processTrailerJsonString(String jsonData) {

        if (jsonData == null) return null;

        // Only interested in youtube trailers
        final String TRAILER_TYPE = "Trailer";
        final String YOUTUBE_SITE = "YouTube";

        try {

            JSONArray jsonArray = new JSONObject(jsonData).getJSONArray(JSON_ARRAY_KEY);
            ArrayList<Trailer> trailerList = new ArrayList<>(jsonArray.length());

            for (int index = 0; index < jsonArray.length(); index++) {

                Trailer video = new Trailer(jsonArray.getJSONObject(index));

                if (video.getType().equals(TRAILER_TYPE) && video.getSite().equals(YOUTUBE_SITE)) {
                    trailerList.add(video);
                }

            }

            Trailer[] trailers = new Trailer[trailerList.size()];
            return trailerList.toArray(trailers);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static Review[] processReviewJsonString(String jsonData) {

        if (jsonData == null) return null;

        Review[] reviews = null;

        try {

            JSONArray jsonArray = new JSONObject(jsonData).getJSONArray(JSON_ARRAY_KEY);

            reviews = new Review[jsonArray.length()];

            for (int index = 0; index < jsonArray.length(); index++) {

                reviews[index] = new Review(jsonArray.getJSONObject(index));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;

    }


}
