package com.adrian971029.filmesfamosos.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.adrian971029.filmesfamosos.R;

public class FilmesFamososContentProvider extends ContentProvider {

    private FilmesFamososDbHelper famososDbHelper;

    private static final int MOVIES          = 500;
    private static final int MOVIES_WITH_ID  = 501;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        String with_id = "/#";
        String path_with_id = FilmesFamososContract.PATH_FILMES_FAMOSOS + with_id;

        uriMatcher.addURI(FilmesFamososContract.AUTHORITY, FilmesFamososContract.PATH_FILMES_FAMOSOS, MOVIES);
        uriMatcher.addURI(FilmesFamososContract.AUTHORITY, path_with_id, MOVIES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        famososDbHelper = new FilmesFamososDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = famososDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor cursor;


        switch (match) {
            case MOVIES:
                cursor = db.query(FilmesFamososContract.FilmesFamososEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.uri_desconhecida) + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = famososDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES:
                long id = db.insert(FilmesFamososContract.FilmesFamososEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FilmesFamososContract.FilmesFamososEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException(getContext().getString(R.string.error_inserir) + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.uri_desconhecida) + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = famososDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);


        int movieDeleted;

        switch (match) {
            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                movieDeleted = db.delete(FilmesFamososContract.FilmesFamososEntry.TABLE_NAME, FilmesFamososContract.FilmesFamososEntry.COLUMN_MOVIE_ID + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.uri_desconhecida) + uri);
        }

        if (movieDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return movieDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
