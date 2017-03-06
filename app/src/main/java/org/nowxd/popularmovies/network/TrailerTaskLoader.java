package org.nowxd.popularmovies.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.nowxd.popularmovies.BuildConfig;
import org.nowxd.popularmovies.model.Trailer;
import org.nowxd.popularmovies.utils.JsonUtils;
import org.nowxd.popularmovies.utils.NetworkUtils;

public class TrailerTaskLoader extends AsyncTaskLoader<Trailer[]> {

    private String movieId;

    public TrailerTaskLoader(Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    public Trailer[] loadInBackground() {

        String jsonData = NetworkUtils.requestMovieTrailers(movieId, BuildConfig.MOVIE_API_KEY);

        return JsonUtils.processTrailerJsonString(jsonData);

    }
}
