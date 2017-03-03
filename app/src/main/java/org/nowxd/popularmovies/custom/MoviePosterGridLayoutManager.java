package org.nowxd.popularmovies.custom;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

/**
 * Custom GridLayoutManager to deal with auto-fitting and calculating sizes of grid items
 *
 * Resource used: http://stackoverflow.com/questions/26666143/recyclerview-gridlayoutmanager-how-to-auto-detect-span-count
 */
public class MoviePosterGridLayoutManager extends GridLayoutManager {

    private static final String TAG = GridLayoutManager.class.getSimpleName();

    private float moviePosterWidth;
    private float moviePosterHeight;
    private float totalWidth;

    public MoviePosterGridLayoutManager(Context context, float moviePosterWidth, float moviePosterHeight) {

        // Will adjust the spanCount and totalWidth in onLayoutChildren
        super(context, 1);
        this.moviePosterWidth = moviePosterWidth;
        this.moviePosterHeight = moviePosterHeight;
        this.totalWidth = 0;

    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        // Before laying out the children, compute the total width we have work with, as well
        // as the number of columns that we should use
        this.totalWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int spanCount = Math.max(2, (int) (totalWidth / moviePosterWidth));

        setSpanCount(spanCount);

        super.onLayoutChildren(recycler, state);

    }

}
