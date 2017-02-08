package org.nowxd.popularmovies.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nowxd.popularmovies.R;
import org.nowxd.popularmovies.data.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView movieTitleTextView;
    private ImageView moviePosterImageView;
    private TextView movieRatingTextView;
    private TextView movieReleaseDateTextView;
    private TextView moviePlotTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieTitleTextView = (TextView) findViewById(R.id.tv_detail_movie_title);
        moviePosterImageView = (ImageView) findViewById(R.id.iv_detail_movie_poster);
        movieRatingTextView = (TextView) findViewById(R.id.tv_user_rating);
        movieReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        moviePlotTextView = (TextView) findViewById(R.id.tv_movie_plot);

        Movie movie = getIntent().getParcelableExtra(getString(R.string.parcelable_movie_key));

        setUpViews(movie);

    }

    private void setUpViews(Movie movie) {

        movieTitleTextView.setText(movie.getTitle());

        Picasso.with(getApplicationContext())
                .load(movie.getPosterImageUrl())
                .fit()
                .into(moviePosterImageView);

        String userRating = String.format(
                getResources().getString(R.string.user_rating),
                movie.getUserRating()
        );

        movieRatingTextView.setText(userRating);

        String releaseDate = String.format(
                getResources().getString(R.string.release_date),
                movie.getReleaseDate()
        );

        movieReleaseDateTextView.setText(releaseDate);

        String moviePlot = String.format(
                getResources().getString(R.string.movie_plot),
                movie.getPlotSynopsis()
        );

        moviePlotTextView.setText(moviePlot);

    }

}
