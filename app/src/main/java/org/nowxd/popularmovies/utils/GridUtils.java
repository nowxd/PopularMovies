package org.nowxd.popularmovies.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class GridUtils {

    /**
     * Calculate number of columns for the GridLayoutManager to use
     * resource: http://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
     */
    public static int calculateNumberOfColumns(Context context, float imageHeightPx) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        // 185 width x 277 height Pixel Ratio
        float imageWidthPx = imageHeightPx * 185 / 277;

        float screenWidthPx = displayMetrics.widthPixels;

        int numberOfColumns = (int) (screenWidthPx / imageWidthPx);

        return numberOfColumns;

    }


}
