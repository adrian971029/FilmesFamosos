package com.adrian971029.filmesfamosos.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adrian971029.filmesfamosos.R;
import com.adrian971029.filmesfamosos.adapter.MovieAdapter;
import com.adrian971029.filmesfamosos.adapter.MovieDataAdapter;
import com.adrian971029.filmesfamosos.data.dao.MovieDAO;
import com.adrian971029.filmesfamosos.io.ApiAdapter;
import com.adrian971029.filmesfamosos.io.ApiService;
import com.adrian971029.filmesfamosos.io.response.PopularResponse;
import com.adrian971029.filmesfamosos.io.response.TopRatedResponse;
import com.adrian971029.filmesfamosos.model.Movie;
import com.adrian971029.filmesfamosos.utils.network.HttpConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private List<Movie> mMovie;
    private List<Movie> mMovieData;
    private GridView mGridView;
    private TextView mTextMensagem;
    private ProgressBar mProgressBar;
    private ArrayAdapter<Movie> mAdapter;
    private Toolbar mToolbar;
    private MovieDAO movieDAO;
    private MovieTask mMovieTask;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializandoComponentes();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setTitle(R.string.action_title);
        exibirProgress(true);
        crearLayoutPopular();

        movieDAO = new MovieDAO(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        menuItem = item;

        switch (id) {
            case R.id.menu_popular_movie:
                mAdapter.clear();
                mMovie.clear();
                crearLayoutPopular();
                break;
            case R.id.menu_top_rated:
                mAdapter.clear();
                mMovie.clear();
                crearLayoutTopRated();
                break;
            case R.id.menu_favorito:
                mAdapter.clear();
                mMovie.clear();
                if(mMovieData != null) {
                    mMovieData.clear();
                }
                crearLayoutFavoritos();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void crearLayoutPopular(){
        if(mMovie == null){
            mMovie = new ArrayList<Movie>();
        }
        mAdapter = new MovieAdapter(getApplicationContext(),mMovie);
        mGridView.setAdapter(mAdapter);

        if(HttpConnection.temConexao(this)){
            chamaFilmesPopular();
        }
        else{
            exibirProgress(false);
            mTextMensagem.setText(R.string.sem_conexao);
        }

    }

    private void crearLayoutTopRated(){
        if(mMovie == null){
            mMovie = new ArrayList<Movie>();
        }
        mAdapter = new MovieAdapter(getApplicationContext(),mMovie);
        mGridView.setAdapter(mAdapter);

        if(HttpConnection.temConexao(this)){
            chamaTopRated();
        }
        else{
            exibirProgress(false);
            mTextMensagem.setText(R.string.sem_conexao);
        }

    }

    private void crearLayoutFavoritos(){
        if(mMovieData == null){
            mMovieData = new ArrayList<Movie>();
        }
        mAdapter = new MovieDataAdapter(getApplicationContext(),mMovieData);
        mGridView.setAdapter(mAdapter);

        if(mMovieTask == null || mMovieTask.getStatus() != AsyncTask.Status.RUNNING){
            mMovieTask = new MovieTask();
            mMovieTask.execute();
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
        mTextMensagem.setVisibility(exibir ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    private void chamaFilmesPopular(){
        ApiService service = ApiAdapter.getApiService();
        Call<PopularResponse> popularResponseCall = service.getPopularMovies();

        popularResponseCall.enqueue(new Callback<PopularResponse>() {
            @Override
            public void onResponse(Call<PopularResponse> call, Response<PopularResponse> response) {
                if(!response.isSuccessful()) {
                    Log.i("TAG","Error:" + response.code());
                    exibirProgress(false);
                }
                else {
                    exibirProgress(false);
                    PopularResponse popularResponse = response.body();
                    if(popularResponse.getResults() != null){
                        mMovie.clear();
                        mMovie.addAll(popularResponse.getResults());
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<PopularResponse> call, Throwable t) {
                exibirProgress(false);
                Log.e("Movies","Error:" + t.getMessage());
            }

        });

    }

    private void chamaTopRated(){
        ApiService service = ApiAdapter.getApiService();
        Call<TopRatedResponse> topRatedResponseCall = service.getTopRatedMovies();

        topRatedResponseCall.enqueue(new Callback<TopRatedResponse>() {
            @Override
            public void onResponse(Call<TopRatedResponse> call, Response<TopRatedResponse> response) {
                if(!response.isSuccessful()) {
                    Log.i("TAG","Error:" + response.code());
                    exibirProgress(false);
                }
                else {
                    exibirProgress(false);
                    TopRatedResponse topRatedResponse = response.body();
                    if(topRatedResponse.getResults() != null){
                        mMovie.clear();
                        mMovie.addAll(topRatedResponse.getResults());
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<TopRatedResponse> call, Throwable t) {
                exibirProgress(false);
                Log.e("Movies","Error:" + t.getMessage());
            }

        });

    }

    class MovieTask extends AsyncTask<Void,Void,List<Movie>> {

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
            exibirProgress(false);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(menuItem != null) {
            int itemControl = menuItem.getItemId();
            switch (itemControl) {
                case R.id.menu_popular_movie:
                    mAdapter.clear();
                    mMovie.clear();
                    crearLayoutPopular();
                    break;
                case R.id.menu_top_rated:
                    mAdapter.clear();
                    mMovie.clear();
                    crearLayoutTopRated();
                    break;
                case R.id.menu_favorito:
                    mAdapter.clear();
                    mMovie.clear();
                    if (mMovieData != null) {
                        mMovieData.clear();
                    }
                    crearLayoutFavoritos();
                    break;
                default:
                    break;
            }
        }
    }
}
