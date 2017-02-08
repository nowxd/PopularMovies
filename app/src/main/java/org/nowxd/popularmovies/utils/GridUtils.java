package org.nowxd.popularmovies.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

public class GridUtils {

    /**
     * Calculate number of columns for the GridLayoutManager to use
     * resource: http://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
     */
    public static int calculateNumberOfColumns(Context context, float imageWidthPx) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        float screenWidthPx = displayMetrics.widthPixels;

        int numberOfColumns = (int) (screenWidthPx / imageWidthPx);

        return numberOfColumns;

    }


}
