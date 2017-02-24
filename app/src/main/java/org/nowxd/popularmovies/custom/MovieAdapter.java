package org.nowxd.popularmovies.custom;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.nowxd.popularmovies.R;
import org.nowxd.popularmovies.data.Movie;
import org.nowxd.popularmovies.database.MovieContract;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private final String TAG = MovieAdapter.class.getSimpleName();

    private Cursor movieCursor;

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

        return new MovieViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        movieCursor.moveToPosition(position);

        int posterIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URL);
        String posterImageUrl = movieCursor.getString(posterIndex);

        holder.setPoster(posterImageUrl);

    }

    @Override
    public int getItemCount() {
        if (movieCursor == null) return 0;
        return movieCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {

        if (movieCursor != null) {
            movieCursor.close();
        }

        movieCursor = newCursor;
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

            Picasso.with(context)
                    .load(posterUrl)
                    .fit()
                    .into(posterImageView);

        }

        @Override
        public void onClick(View view) {
            // TODO change Detail View to use a cursor and not a POJO
//            onClickListener.onMoviePosterClick(movieData[getAdapterPosition()]);
        }
    }

}
