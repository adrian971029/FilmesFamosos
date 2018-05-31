package com.adrian971029.filmesfamosos.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FilmesFamososDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "filmes_famosos.db";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOVIES = "CREATE TABLE " +
            FilmesFamososContract.FilmesFamososEntry.TABLE_NAME + " (" +
            FilmesFamososContract.FilmesFamososEntry._ID + " INTEGER PRIMARY KEY, " +
            FilmesFamososContract.FilmesFamososEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
            FilmesFamososContract.FilmesFamososEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
            FilmesFamososContract.FilmesFamososEntry.COLUMN_ADULT + " INTEGER NOT NULL, " +
            FilmesFamososContract.FilmesFamososEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
            FilmesFamososContract.FilmesFamososEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
            FilmesFamososContract.FilmesFamososEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
            FilmesFamososContract.FilmesFamososEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            FilmesFamososContract.FilmesFamososEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
            FilmesFamososContract.FilmesFamososEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL" +
            "); ";

    public FilmesFamososDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MOVIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FilmesFamososContract.FilmesFamososEntry.TABLE_NAME);
    }
}
