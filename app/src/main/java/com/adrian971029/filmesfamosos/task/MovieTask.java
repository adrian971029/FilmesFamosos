package com.adrian971029.filmesfamosos.task;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adrian971029.filmesfamosos.data.dao.MovieDAO;
import com.adrian971029.filmesfamosos.model.Movie;

import java.util.List;

public class MovieTask extends AsyncTask<Void,Void,List<Movie>> {

    private MovieDAO movieDAO;
    private List<Movie> mMovieData;
    private ArrayAdapter<Movie> mAdapter;
    private TextView mTextMensagem;

    public MovieTask(MovieDAO movieDAO, List<Movie> mMovieData, ArrayAdapter<Movie> mAdapter, TextView mTextMensagem) {
        this.movieDAO = movieDAO;
        this.mMovieData = mMovieData;
        this.mAdapter = mAdapter;
        this.mTextMensagem = mTextMensagem;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Movie> doInBackground(Void... voids) {
        return movieDAO.buscarTodos();
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        if(movies != null){
            mMovieData.clear();
            mMovieData.addAll(movies);
            mAdapter.notifyDataSetChanged();
        }
        else {
            mTextMensagem.setText("Não tem filmes favoritos");
        }
    }

}
