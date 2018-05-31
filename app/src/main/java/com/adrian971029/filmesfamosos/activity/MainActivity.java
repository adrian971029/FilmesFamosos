package com.adrian971029.filmesfamosos.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.adrian971029.filmesfamosos.task.MovieTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private static final String TAG  = Movie.class.getSimpleName();

    @BindView(R.id.grid_view) GridView mGridView;
    @BindView(R.id.tv_aguarde) TextView mTextMensagem;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.tool_bar) Toolbar mToolbar;
    private List<Movie> mMovie;
    private List<Movie> mMovieData;
    private ArrayAdapter<Movie> mAdapter;
    private MovieDAO movieDAO;
    private MovieTask mMovieTask;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mGridView.setEmptyView(mTextMensagem);
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

        if(temConexao(this)){
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

        if(temConexao(this)){
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
            mMovieTask = new MovieTask(movieDAO,mMovieData,mAdapter,mTextMensagem);
            mMovieTask.execute();
        }

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
                    Log.i(TAG,"Error:" + response.code());
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
                Log.e(TAG,"Error:" + t.getMessage());
            }

        });

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
