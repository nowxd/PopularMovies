package org.nowxd.popularmovies.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.nowxd.popularmovies.BuildConfig;
import org.nowxd.popularmovies.model.Review;
import org.nowxd.popularmovies.utils.JsonUtils;
import org.nowxd.popularmovies.utils.NetworkUtils;

public class ReviewTaskLoader extends AsyncTaskLoader<Review[]>{

    private String movieId;

    public ReviewTaskLoader(Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    public Review[] loadInBackground() {

        String jsonData = NetworkUtils.requestMovieReviews(movieId, BuildConfig.MOVIE_API_KEY);

        return JsonUtils.processReviewJsonString(jsonData);

    }
}
