package com.adrian971029.filmesfamosos.data;

import android.provider.BaseColumns;

public class MoviesContract {

    public static class MoviesEntry implements BaseColumns {

        private static final String TABLE_NAME = "movies";
        private static final String COLUMN_POSTER_PATH = "poster_path";
        private static final String COLUMN_ADULT = "adult";
        private static final String COLUMN_OVERVIEW = "overview";
        private static final String COLUMN_RELEASE_DATE = "release_date";
        private static final String COLUMN_GENRE_IDS = "id";
        private static final String COLUMN_ORIGINAL_TITLE = "original_title";
        private static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        private static final String COLUMN_TITLE = "title";
        private static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        private static final String COLUMN_POPULARITY = "popularity";
        private static final String COLUMN_VOTE_COUNT = "vote_count";
        private static final String COLUMN_VIDEO = "video";
        private static final String COLUMN_VOTE_AVERAGE = "vote_average";

    }

}
