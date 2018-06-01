package com.adrian971029.filmesfamosos.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.adrian971029.filmesfamosos.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class ContentProviderAccess {

    public static void inserirContentProvider(Context context, Movie movie) throws Exception {
        ContentValues values = new ContentValues();
        values.put(FilmesFamososContract.FilmesFamososEntry.COLUMN_MOVIE_ID,movie.getId());
        values.put(FilmesFamososContract.FilmesFamososEntry.COLUMN_POSTER_PATH,movie.getPoster_path());
        values.put(FilmesFamososContract.FilmesFamososEntry.COLUMN_ADULT,movie.isAdult());
        values.put(FilmesFamososContract.FilmesFamososEntry.COLUMN_OVERVIEW,movie.getOverview());
        values.put(FilmesFamososContract.FilmesFamososEntry.COLUMN_RELEASE_DATE,movie.getRelease_date());
        values.put(FilmesFamososContract.FilmesFamososEntry.COLUMN_ORIGINAL_TITLE,movie.getOriginal_title());
        values.put(FilmesFamososContract.FilmesFamososEntry.COLUMN_TITLE,movie.getTitle());
        values.put(FilmesFamososContract.FilmesFamososEntry.COLUMN_BACKDROP_PATH,movie.getPoster_path());
        values.put(FilmesFamososContract.FilmesFamososEntry.COLUMN_VOTE_AVERAGE,movie.getVote_average());

        context.getContentResolver().insert(FilmesFamososContract.FilmesFamososEntry.CONTENT_URI, values);

    }

    public static List<Movie> buscarTodos(Context context) {

        FilmesFamososDbHelper famososDbHelper = new FilmesFamososDbHelper(context);
        final SQLiteDatabase db = famososDbHelper.getReadableDatabase();

        List<Movie> listaMovies = new ArrayList<>();

        String[] columns = {FilmesFamososContract.FilmesFamososEntry.COLUMN_MOVIE_ID,
                FilmesFamososContract.FilmesFamososEntry.COLUMN_POSTER_PATH,
                FilmesFamososContract.FilmesFamososEntry.COLUMN_ADULT,
                FilmesFamososContract.FilmesFamososEntry.COLUMN_OVERVIEW,
                FilmesFamososContract.FilmesFamososEntry.COLUMN_RELEASE_DATE,
                FilmesFamososContract.FilmesFamososEntry.COLUMN_ORIGINAL_TITLE,
                FilmesFamososContract.FilmesFamososEntry.COLUMN_TITLE,
                FilmesFamososContract.FilmesFamososEntry.COLUMN_BACKDROP_PATH,
                FilmesFamososContract.FilmesFamososEntry.COLUMN_VOTE_AVERAGE
        };

        Cursor cursor = db.query(FilmesFamososContract.FilmesFamososEntry.TABLE_NAME,columns,null,null,null,null,null);

        if (cursor.moveToFirst()) {

            do {
                Movie movies = new Movie();
                movies.setId(cursor.getLong(cursor.getColumnIndex(columns[0])));
                movies.setPoster_path(cursor.getString(cursor.getColumnIndex(columns[1])));
                movies.setAdult(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(columns[2]))));
                movies.setOverview(cursor.getString(cursor.getColumnIndex(columns[3])));
                movies.setRelease_date(cursor.getString(cursor.getColumnIndex(columns[4])));
                movies.setOriginal_title(cursor.getString(cursor.getColumnIndex(columns[5])));
                movies.setTitle(cursor.getString(cursor.getColumnIndex(columns[6])));
                movies.setBackdrop_path(cursor.getString(cursor.getColumnIndex(columns[7])));
                movies.setVote_average(cursor.getFloat(cursor.getColumnIndex(columns[8])));

                listaMovies.add(movies);

            }while (cursor.moveToNext());

            return listaMovies;

        }

        return null;
    }

    public static boolean deletar(Movie movie, String id, Context context) throws Exception {

        FilmesFamososDbHelper famososDbHelper = new FilmesFamososDbHelper(context);
        final SQLiteDatabase db = famososDbHelper.getReadableDatabase();

        String idString = id;
        Uri uri = FilmesFamososContract.FilmesFamososEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(idString).build();

        return context.getContentResolver().delete(uri,null,null) > 0;

    }

}
