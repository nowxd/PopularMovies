package org.nowxd.popularmovies.database;

import android.provider.BaseColumns;

public class MovieContract {

    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_PLOT = "plot";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";

    }

}
