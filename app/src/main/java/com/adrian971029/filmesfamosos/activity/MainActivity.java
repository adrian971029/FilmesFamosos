package com.adrian971029.filmesfamosos.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adrian971029.filmesfamosos.R;
import com.adrian971029.filmesfamosos.adapter.MovieAdapter;
import com.adrian971029.filmesfamosos.model.Movie;
import com.adrian971029.filmesfamosos.utils.MovieHttp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MovieTask mTask;
    private List<Movie> mMovie;
    private GridView mGridView;
    private TextView mTextMensagem;
    private ProgressBar mProgressBar;
    private ArrayAdapter<Movie> mAdapter;
    private Toolbar mToolbar;
    private boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flag = false;
        inicializandoComponentes();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setTitle(R.string.action_title);
        crearLayoutPrincipal();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_popular_movie){
            flag = false;
            mTask = null;
            mAdapter.clear();
            mMovie.clear();
            crearLayoutPrincipal();
        }
        else if(id == R.id.menu_top_rated){
            flag = true;
            mTask = null;
            mAdapter.clear();
            mMovie.clear();
            crearLayoutPrincipal();
        }

        return super.onOptionsItemSelected(item);
    }

    private void crearLayoutPrincipal(){
        if(mMovie == null){
            mMovie = new ArrayList<Movie>();
        }
        mAdapter = new MovieAdapter(getApplicationContext(),mMovie);
        mGridView.setAdapter(mAdapter);
        if(mTask == null){
            if(MovieHttp.temConexao(this)){
                iniciarDownload();
            }
            else{
                mTextMensagem.setText(R.string.sem_conexao);
            }
        }
        else if(mTask.getStatus() == AsyncTask.Status.RUNNING){
            exibirProgress(true);
        }
    }

    private void inicializandoComponentes(){
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mTextMensagem = (TextView)findViewById(R.id.tv_aguarde);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mGridView = (GridView)findViewById(R.id.grid_view);
        mGridView.setEmptyView(mTextMensagem);
    }

    private void exibirProgress(boolean exibir){
        if(exibir){
            mTextMensagem.setText(R.string.baixando_filmes);
        }
        mTextMensagem.setVisibility(exibir ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    private void iniciarDownload(){
        if(mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING){
            mTask = new MovieTask();
            mTask.execute();
        }
    }

    class MovieTask extends AsyncTask<Void,Void,List<Movie>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            if (!flag){
                return MovieHttp.carregarMoviePopularJson();
            }
            else {
                return MovieHttp.carregarMovieTopRated();
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            exibirProgress(false);
            if(movies != null){
                mMovie.clear();
                mMovie.addAll(movies);
                mAdapter.notifyDataSetChanged();
            }
            else {
                mTextMensagem.setText(R.string.falha_filmes);
            }
        }
    }

}
