package org.nowxd.popularmovies.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import org.nowxd.popularmovies.R;

public class GridUtils {

    /**
     * Calculate number of columns for the GridLayoutManager to use
     * resource: http://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
     */
    public static int calculateNumberOfColumns(Context context) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        float screenWidthPx = displayMetrics.widthPixels;
        float imageWidthPx = context.getResources().getDimension(R.dimen.movie_poster_detail_width);

        int numberOfColumns = Math.max(2, (int) (screenWidthPx / imageWidthPx));

        return numberOfColumns;

    }

}
