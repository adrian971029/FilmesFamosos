package com.adrian971029.filmesfamosos.data;

import android.provider.BaseColumns;

public class FilmesFamososContract {

    public static class FilmesFamososEntry implements BaseColumns {

        public static final String COLUMN_MOVIE_ID = "id";
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

    }

}
