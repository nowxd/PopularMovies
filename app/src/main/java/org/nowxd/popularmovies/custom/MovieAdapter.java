package org.nowxd.popularmovies.custom;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.nowxd.popularmovies.R;
import org.nowxd.popularmovies.data.Movie;
import org.nowxd.popularmovies.utils.GridUtils;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private final String TAG = MovieAdapter.class.getSimpleName();

    private Movie[] movieData;
    private Context context;
    private MoviePosterOnClickListener onClickListener;

    public MovieAdapter(Context context, MoviePosterOnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_item, parent, false);

        // Compute the width and height of a GridView item and set it as the view's layout_params
        int parent_width = parent.getMeasuredWidth();

        int columns = GridUtils.calculateNumberOfColumns(
                context,
                context.getResources().getDimension(R.dimen.movie_poster_width)
        );

        final double RATIO = context.getResources().getDimension(R.dimen.movie_poster_height) /
                context.getResources().getDimension(R.dimen.movie_poster_width);

        int width = parent_width / columns;
        int height = (int) (width * RATIO);

        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(width, height);
        view.setLayoutParams(params);

        return new MovieViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.setPoster(movieData[position].getPosterImageUrl());
    }

    @Override
    public int getItemCount() {
        if (movieData == null) return 0;
        return movieData.length;
    }

    public void setMovieData(Movie[] movieData) {
        this.movieData = movieData;
        notifyDataSetChanged();
    }

    /**
     * OnClickListener
     */
    public interface MoviePosterOnClickListener {
        void onMoviePosterClick(Movie movie);
    }

    /**
     * ViewHolder
    */
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView posterImageView;

        MovieViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            posterImageView.setOnClickListener(this);
        }

        void setPoster(String posterUrl) {

//            Log.d(TAG, "setPoster: " + posterImageView.getWidth() + " : " + posterImageView.getHeight());

            Picasso.with(context)
                    .load(posterUrl)
                    .fit()
                    .into(posterImageView);

        }

        @Override
        public void onClick(View view) {
            onClickListener.onMoviePosterClick(movieData[getAdapterPosition()]);
        }
    }

}
