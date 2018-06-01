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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adrian971029.filmesfamosos.R;
import com.adrian971029.filmesfamosos.adapter.MovieAdapter;
import com.adrian971029.filmesfamosos.adapter.MovieDataAdapter;
import com.adrian971029.filmesfamosos.data.ContentProviderAccess;
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
    private static final int POPULAR_MOVIE  = 1;
    private static final int TOP_RATED  = 2;
    private static final int FAVORITES  = 3;

    @BindView(R.id.grid_view) GridView mGridView;
    @BindView(R.id.tv_aguarde) TextView mTextMensagem;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.tool_bar) Toolbar mToolbar;
    @BindView(R.id.img_SemConexao) ImageView imgSemConexao;
    @BindView(R.id.tv_semConexao) TextView mTextMensagemSemConexao;
    @BindView(R.id.img_SemFavoritos) ImageView imgSemFavoritos;
    @BindView(R.id.tv_semFavoritos) TextView mTextMensagemSemFavoritos;
    @BindView(R.id.img_atualizar) ImageView imgAtualizar;
    private List<Movie> mMovie;
    private List<Movie> mMovieData;
    private ArrayAdapter<Movie> mAdapter;
    private ContentProviderAccess providerAccess;
    private MovieTask mMovieTask;
    private MenuItem menuItem;
    private int controlLayout;
    public static boolean temFavoritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        temFavoritos = false;
        exibirMensagemSemConexao(false);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setTitle(R.string.action_title);
        exibirProgress(true);
        crearLayoutPopular();

        providerAccess = new ContentProviderAccess();

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
                exibirProgress(true);
                exibirMensagemFavoritos(false);
                exibirMensagemSemConexao(false);
                mAdapter.clear();
                mMovie.clear();
                crearLayoutPopular();
                break;
            case R.id.menu_top_rated:
                exibirProgress(true);
                exibirMensagemFavoritos(false);
                exibirMensagemSemConexao(false);
                mAdapter.clear();
                mMovie.clear();
                crearLayoutTopRated();
                break;
            case R.id.menu_favorito:
                exibirProgress(false);
                exibirMensagemFavoritos(false);
                exibirMensagemSemConexao(false);
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
        controlLayout = POPULAR_MOVIE;
        if(mMovie == null){
            mMovie = new ArrayList<Movie>();
        }
        mAdapter = new MovieAdapter(getApplicationContext(),mMovie);
        mGridView.setAdapter(mAdapter);

        if(temConexao(this)){
            exibirMensagemSemConexao(false);
            chamaFilmesPopular();
        }
        else{
            exibirProgress(false);
            exibirMensagemSemConexao(true);
            Toast.makeText(this, R.string.lbl_tente_novamente,Toast.LENGTH_SHORT).show();
        }

    }

    private void crearLayoutTopRated(){
        controlLayout = TOP_RATED;
        if(mMovie == null){
            mMovie = new ArrayList<Movie>();
        }
        mAdapter = new MovieAdapter(getApplicationContext(),mMovie);
        mGridView.setAdapter(mAdapter);

        if(temConexao(this)){
            exibirMensagemSemConexao(false);
            chamaTopRated();
        }
        else{
            exibirProgress(false);
            exibirMensagemSemConexao(true);
            Toast.makeText(this, R.string.lbl_tente_novamente,Toast.LENGTH_SHORT).show();
        }

    }

    private void crearLayoutFavoritos(){
        controlLayout = FAVORITES;
        exibirProgress(false);
        if(mMovieData == null){
            mMovieData = new ArrayList<Movie>();
        }
        mAdapter = new MovieDataAdapter(getApplicationContext(),mMovieData);
        mGridView.setAdapter(mAdapter);

        if(mMovieTask == null || mMovieTask.getStatus() != AsyncTask.Status.RUNNING){
            mMovieTask = new MovieTask(this,providerAccess,mMovieData,mAdapter,imgSemFavoritos,mTextMensagemSemFavoritos);
            mMovieTask.execute();
        }

    }

    private void exibirProgress(boolean exibir){
        mTextMensagem.setVisibility(exibir ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    private void exibirMensagemSemConexao(boolean exibir) {
        imgSemConexao.setVisibility(exibir ? View.VISIBLE : View.GONE);
        mTextMensagemSemConexao.setVisibility(exibir ? View.VISIBLE : View.GONE);
        imgAtualizar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    private void exibirMensagemFavoritos(boolean exibir) {
        imgSemFavoritos.setVisibility(exibir ? View.VISIBLE : View.GONE);
        mTextMensagemSemFavoritos.setVisibility(exibir ? View.VISIBLE : View.GONE);
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
                    exibirProgress(true);
                    exibirMensagemFavoritos(false);
                    exibirMensagemSemConexao(false);
                    mAdapter.clear();
                    mMovie.clear();
                    crearLayoutPopular();
                    break;
                case R.id.menu_top_rated:
                    exibirProgress(true);
                    exibirMensagemFavoritos(false);
                    exibirMensagemSemConexao(false);
                    mAdapter.clear();
                    mMovie.clear();
                    crearLayoutTopRated();
                    break;
                case R.id.menu_favorito:
                    exibirProgress(false);
                    exibirMensagemFavoritos(false);
                    exibirMensagemSemConexao(false);
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

    public void atualizar(View view) {
        switch (controlLayout) {
                case POPULAR_MOVIE:
                    exibirProgress(true);
                    exibirMensagemFavoritos(false);
                    exibirMensagemSemConexao(false);
                    mAdapter.clear();
                    mMovie.clear();
                    crearLayoutPopular();
                    break;
                case TOP_RATED:
                    exibirProgress(true);
                    exibirMensagemFavoritos(false);
                    exibirMensagemSemConexao(false);
                    mAdapter.clear();
                    mMovie.clear();
                    crearLayoutTopRated();
                    break;
                case FAVORITES:
                    exibirProgress(false);
                    exibirMensagemFavoritos(false);
                    exibirMensagemSemConexao(false);
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
