package com.adrian971029.filmesfamosos.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.adrian971029.filmesfamosos.data.FilmesFamososContract;
import com.adrian971029.filmesfamosos.data.FilmesFamososDbHelper;
import com.adrian971029.filmesfamosos.data.SQLiteDAO;
import com.adrian971029.filmesfamosos.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDAO  implements SQLiteDAO<Movie>{

    private SQLiteDatabase database;

    public MovieDAO(Context context) {
        FilmesFamososDbHelper dbHelper = new FilmesFamososDbHelper(context);
        database = dbHelper.getWritableDatabase();
    }


    @Override
    public long inserir(Movie movie) throws Exception {
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

        return database.insert(FilmesFamososContract.FilmesFamososEntry.TABLE_NAME,null,values);

    }

    @Override
    public void atualizar(Movie movie) throws Exception {
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

        String where = "id = ?";
        String[] args = {String.valueOf(movie.getId())};

        database.update(FilmesFamososContract.FilmesFamososEntry.TABLE_NAME, values, where, args);

    }

    @Override
    public boolean deletar(Movie movie,String id) throws Exception {

        String where = FilmesFamososContract.FilmesFamososEntry.COLUMN_MOVIE_ID + "=" + id;

        return database.delete(FilmesFamososContract.FilmesFamososEntry.TABLE_NAME, where, null) > 0;

    }

    @Override
    public Movie buscar(long id) {

        Movie movies;

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

        String where = "id = ?";
        String[] args = {String.valueOf(id)};

        Cursor cursor = database.query(FilmesFamososContract.FilmesFamososEntry.TABLE_NAME,columns,where,args,null,null,null);
        if (cursor.moveToFirst()) {
            movies = new Movie();
            movies.setId(cursor.getLong(cursor.getColumnIndex(columns[0])));
            movies.setPoster_path(cursor.getString(cursor.getColumnIndex(columns[1])));
            movies.setAdult(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(columns[2]))));
            movies.setOverview(cursor.getString(cursor.getColumnIndex(columns[3])));
            movies.setRelease_date(cursor.getString(cursor.getColumnIndex(columns[4])));
            movies.setOriginal_title(cursor.getString(cursor.getColumnIndex(columns[5])));
            movies.setTitle(cursor.getString(cursor.getColumnIndex(columns[6])));
            movies.setBackdrop_path(cursor.getString(cursor.getColumnIndex(columns[7])));
            movies.setVote_average(cursor.getFloat(cursor.getColumnIndex(columns[8])));

            return movies;

        }

        return null;
    }

    @Override
    public List<Movie> buscarTodos() {

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

        Cursor cursor = database.query(FilmesFamososContract.FilmesFamososEntry.TABLE_NAME,columns,null,null,null,null,null);

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
}
