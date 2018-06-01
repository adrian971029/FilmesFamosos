package com.adrian971029.filmesfamosos.task;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adrian971029.filmesfamosos.data.ContentProviderAccess;
import com.adrian971029.filmesfamosos.model.Movie;

import java.util.List;

public class MovieTask extends AsyncTask<Void,Void,List<Movie>> {

    private ContentProviderAccess providerAccess;
    private List<Movie> mMovieData;
    private ArrayAdapter<Movie> mAdapter;
    private ImageView imgSemFavoritos;
    private TextView mTextMensagemSemFavoritos;
    private Context context;

    public MovieTask(Context context, ContentProviderAccess providerAccess, List<Movie> mMovieData, ArrayAdapter<Movie> mAdapter, ImageView imgSemFavoritos, TextView mTextMensagemSemFavoritos) {
        this.context = context;
        this.providerAccess = providerAccess;
        this.mMovieData = mMovieData;
        this.mAdapter = mAdapter;
        this.imgSemFavoritos = imgSemFavoritos;
        this.mTextMensagemSemFavoritos = mTextMensagemSemFavoritos;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Movie> doInBackground(Void... voids) {
        return providerAccess.buscarTodos(context);
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        if(movies != null){
            if(movies.size() > 0){
                imgSemFavoritos.setVisibility(View.GONE);
                mTextMensagemSemFavoritos.setVisibility(View.GONE);
                mMovieData.clear();
                mMovieData.addAll(movies);
                mAdapter.notifyDataSetChanged();
            }else {
                imgSemFavoritos.setVisibility(View.VISIBLE);
                mTextMensagemSemFavoritos.setVisibility(View.VISIBLE);
            }
        }
        else {
            imgSemFavoritos.setVisibility(View.VISIBLE);
            mTextMensagemSemFavoritos.setVisibility(View.VISIBLE);
        }

    }



}
