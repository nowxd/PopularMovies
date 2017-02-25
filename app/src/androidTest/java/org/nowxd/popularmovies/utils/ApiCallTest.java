package org.nowxd.popularmovies.utils;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nowxd.popularmovies.BuildConfig;
import org.nowxd.popularmovies.data.Review;
import org.nowxd.popularmovies.data.Trailer;

@RunWith(AndroidJUnit4.class)
public class ApiCallTest {

    private static final String TAG = ApiCallTest.class.getSimpleName();

    private static final String MOVIE_API_ID = "278";

    // Values that i'm expecting at the time i'm testing this
    private static final int EXPECTED_TRAILER_RESULTS = 3;
    private static final int EXPECTED_REVIEW_RESULTS = 2;

    @Test
    public void testTrailerCall() {

        String jsonData = NetworkUtils.requestMovieTrailers(MOVIE_API_ID, BuildConfig.MOVIE_API_KEY);

        Assert.assertNotNull(jsonData);
        Assert.assertTrue(jsonData.length() > 0);

        Trailer[] trailers = JsonUtils.processTrailerJsonString(jsonData);

        // At the time i'm testing this, I am expecting exactly 3 results
        Assert.assertTrue(trailers.length == EXPECTED_TRAILER_RESULTS);

        for (Trailer trailer : trailers) {

            Log.d(TAG, "testTrailerCall: " + trailer);

        }

    }

    @Test
    public void testReviewCall() {

        String jsonData = NetworkUtils.requestMovieReviews(MOVIE_API_ID, BuildConfig.MOVIE_API_KEY);

        Assert.assertNotNull(jsonData);
        Assert.assertTrue(jsonData.length() > 0);

        Review[] reviews = JsonUtils.processReviewJsonString(jsonData);

        Assert.assertTrue(reviews.length == EXPECTED_REVIEW_RESULTS);

        for (Review review : reviews) {
            Log.d(TAG, "testReviewCall: " + review);
        }

    }

}
