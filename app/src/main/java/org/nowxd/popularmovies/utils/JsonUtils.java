package org.nowxd.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nowxd.popularmovies.data.Movie;

public class JsonUtils {

    private static final String MOVIE_JSON_ARRAY_KEY = "results";

    public static Movie[] processMovieJsonString(String jsonData) {

        Movie[] res = null;

        try {

            JSONArray jsonArray = new JSONObject(jsonData).getJSONArray(MOVIE_JSON_ARRAY_KEY);
            res = new Movie[jsonArray.length()];

            for (int index = 0; index < jsonArray.length(); index++) {

                res[index] = new Movie(jsonArray.getJSONObject(index));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;

    }

}
