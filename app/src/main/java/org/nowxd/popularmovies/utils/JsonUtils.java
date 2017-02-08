package org.nowxd.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nowxd.popularmovies.data.Movie;

public class JsonUtils {

    private static final String MOVIE_JSON_ARRAY_KEY = "results";

    /**
     * Convert the JSON string into an array of movies
     */
    public static Movie[] processMovieJsonString(String jsonData) {

        Movie[] movies = null;

        try {

            JSONArray jsonArray = new JSONObject(jsonData).getJSONArray(MOVIE_JSON_ARRAY_KEY);
            movies = new Movie[jsonArray.length()];

            for (int index = 0; index < jsonArray.length(); index++) {

                movies[index] = new Movie(jsonArray.getJSONObject(index));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;

    }

}
