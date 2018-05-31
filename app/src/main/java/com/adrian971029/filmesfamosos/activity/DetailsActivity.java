package com.adrian971029.filmesfamosos.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adrian971029.filmesfamosos.R;
import com.adrian971029.filmesfamosos.adapter.ReviewAdapter;
import com.adrian971029.filmesfamosos.adapter.VideoAdapter;
import com.adrian971029.filmesfamosos.data.dao.MovieDAO;
import com.adrian971029.filmesfamosos.io.ApiAdapter;
import com.adrian971029.filmesfamosos.io.ApiService;
import com.adrian971029.filmesfamosos.io.response.ReviewResponse;
import com.adrian971029.filmesfamosos.io.response.VideoResponse;
import com.adrian971029.filmesfamosos.model.Movie;
import com.adrian971029.filmesfamosos.model.Review;
import com.adrian971029.filmesfamosos.model.Video;
import com.adrian971029.filmesfamosos.utils.Constants;
import com.adrian971029.filmesfamosos.utils.network.HttpConnection;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends BaseActivity {

    private ImageView mPosterPath,mBackdropPath;
    private TextView mOverview,mTitle,mOriginalTitle,mReleaseDate,mAdult,mVoteAverage;
    private String poster_path,backdrop_path,overview,title,original_title,realese_date;
    private long id_movie;
    private boolean adult;
    private float vote_average;
    private List<Video> mVideo;
    private List<Review> mReview;
    private ArrayAdapter<Video> mAdapterVideo;
    private ArrayAdapter<Review> mAdapterReview;
    private ListView mListViewVideo;
    private ListView mListViewReview;
    private ProgressBar mProgressBarVideo;
    private ProgressBar mProgressBarReview;
    private TextView mTextMensagemVideo;
    private TextView mTextMensagemReview;
    private ImageView mFavoritoImageView, mNaoFavoritoImageView;
    private boolean controlFavorito;
    private MovieDAO movieDAO;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        controlFavorito = false;

        inicializandoComponentes();
        recibiendoValores();
        mostrandoValores();
        exibirProgressVideo(true);
        exibirProgressReview(true);
        controlFavorito();
        crearLayoutTrailer();
        crearLayoutReview();

    }

    private void inicializandoComponentes(){
        mPosterPath = (ImageView)findViewById(R.id.imgPoster_Path);
        mBackdropPath = (ImageView)findViewById(R.id.imgBackdrop_Path);
        mOverview = (TextView)findViewById(R.id.tv_overview);
        mTitle = (TextView)findViewById(R.id.tv_title);
        mOriginalTitle = (TextView)findViewById(R.id.tv_originalTitle);
        mAdult = (TextView)findViewById(R.id.tv_adult);
        mReleaseDate = (TextView)findViewById(R.id.tv_releaseDate);
        mVoteAverage = (TextView)findViewById(R.id.tv_voteAverage);
        mListViewVideo = (ListView)findViewById(R.id.lv_trailerVideo);
        mListViewReview = (ListView)findViewById(R.id.lv_reviews);
        mProgressBarVideo = (ProgressBar)findViewById(R.id.progressBarVideo);
        mTextMensagemVideo = (TextView)findViewById(R.id.tv_aguardeVideo);
        mProgressBarReview = (ProgressBar)findViewById(R.id.progressBarReviews);
        mTextMensagemReview = (TextView)findViewById(R.id.tv_aguardeReviews);
        mFavoritoImageView = (ImageView)findViewById(R.id.img_simfavorito);
        mNaoFavoritoImageView = (ImageView)findViewById(R.id.img_naofavorito);
    }

    private void recibiendoValores(){
        Intent intent = getIntent();
        id_movie = intent.getLongExtra("ID_MOVIE",0);
        poster_path = intent.getStringExtra("POSTER_PATH");
        backdrop_path = intent.getStringExtra("BACKDROP_PATH");
        overview = intent.getStringExtra("OVERVIEW");
        title = intent.getStringExtra("TITLE");
        original_title = intent.getStringExtra("ORIGINAL_TITLE");
        realese_date = intent.getStringExtra("RELEASE_DATE");
        adult = intent.getBooleanExtra("ADULT",false);
        vote_average = intent.getFloatExtra("VOTE_AVERAGE",0);
    }

    private void mostrandoValores(){
        Picasso.with(getApplicationContext()).load(Constants.GET_IMAGES + poster_path).into(mPosterPath);
        Picasso.with(getApplicationContext()).load(Constants.GET_IMAGES + backdrop_path).into(mBackdropPath);
        mTitle.append(title);
        mOriginalTitle.append(original_title);
        if (adult == true){
            mAdult.append("Sim");
        }
        else{
            mAdult.append("Não");
        }
        mVoteAverage.append("" + vote_average);
        mReleaseDate.append(realese_date);
        mOverview.append(overview);
    }

    private void crearLayoutTrailer(){
        if(mVideo == null){
            mVideo = new ArrayList<Video>();
        }

        mAdapterVideo = new VideoAdapter(getApplicationContext(),mVideo);
        mListViewVideo.setAdapter(mAdapterVideo);

        if(HttpConnection.temConexao(this)){
            chamaVideos();
        }
        else{
            mTextMensagemReview.setText(R.string.sem_conexao);
        }

    }

    private void crearLayoutReview(){
        if(mReview == null){
            mReview = new ArrayList<Review>();
        }

        mAdapterReview = new ReviewAdapter(getApplicationContext(),mReview);
        mListViewReview.setAdapter(mAdapterReview);

        if(HttpConnection.temConexao(this)){
                chamaReviews();
        }
        else{
            mTextMensagemReview.setText(R.string.sem_conexao);
        }

    }

    private void exibirProgressVideo(boolean exibir){
        if(exibir){
            mTextMensagemVideo.setText(R.string.baixando_informacoes);
        }
        mTextMensagemVideo.setVisibility(exibir ? View.VISIBLE : View.GONE);
        mProgressBarVideo.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    private void exibirProgressReview(boolean exibir){
        if(exibir){
            mTextMensagemReview.setText(R.string.baixando_informacoes);
        }
        mTextMensagemReview.setVisibility(exibir ? View.VISIBLE : View.GONE);
        mProgressBarReview.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    private void chamaVideos(){
        ApiService service = ApiAdapter.getApiService();
        Call<VideoResponse> videoResponseCall = service.getVideos(String.valueOf(id_movie));

        videoResponseCall.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if(!response.isSuccessful()) {
                    Log.i("TAG","Error:" + response.code());
                    exibirProgressVideo(false);
                }
                else {
                    exibirProgressVideo(false);
                    VideoResponse videoResponse = response.body();
                    if(videoResponse.getResults() != null){
                        mVideo.clear();
                        mVideo.addAll(videoResponse.getResults());
                        mAdapterVideo.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                exibirProgressVideo(false);
                Log.e("Movies","Error:" + t.getMessage());
            }

        });

    }

    private void chamaReviews(){
        ApiService service = ApiAdapter.getApiService();
        Call<ReviewResponse> reviewResponseCall = service.getReviews(String.valueOf(id_movie));

        reviewResponseCall.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if(!response.isSuccessful()) {
                    Log.i("TAG","Error:" + response.code());
                    exibirProgressReview(false);
                }
                else {
                    exibirProgressReview(false);
                    ReviewResponse reviewResponse = response.body();
                    if(reviewResponse.getResults() != null){
                        mReview.clear();
                        mReview.addAll(reviewResponse.getResults());
                        mAdapterReview.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                exibirProgressReview(false);
                Log.e("Movies","Error:" + t.getMessage());
            }

        });

    }

    public void filmeFavorito(View view) {
       if (!deletarCadastroBanco(String.valueOf(id_movie))) return;
       defineVisivilidadFilmeFavorito(false);
       guardandoFavorito(false);
    }

    public void filmeNaoFavorito(View view) {
        if (!inserirCadastroBanco()) return;
        defineVisivilidadFilmeFavorito(true);
        guardandoFavorito(true);
    }

    private void guardandoFavorito(boolean favorito){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(String.valueOf(id_movie),favorito);
        editor.apply();
    }

    private void defineVisivilidadFilmeFavorito(boolean isFavorito) {
        if(isFavorito) {
            mNaoFavoritoImageView.setVisibility(View.GONE);
            mFavoritoImageView.setVisibility(View.VISIBLE);
        }
        else {
            mFavoritoImageView.setVisibility(View.GONE);
            mNaoFavoritoImageView.setVisibility(View.VISIBLE);
        }
    }

    private void controlFavorito() {
        if(sharedPrefs != null) {
            controlFavorito = sharedPrefs.getBoolean(String.valueOf(id_movie),false);
        }

        if(controlFavorito) {
            defineVisivilidadFilmeFavorito(true);
        }
        else {
            defineVisivilidadFilmeFavorito(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        controlFavorito();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        controlFavorito();
    }

    private boolean inserirCadastroBanco() {
        movieDAO = new MovieDAO(this);
        boolean sucesso = false;

        try {

            movie = new Movie();
            movie.setId(id_movie);
            movie.setPoster_path(poster_path);
            movie.setAdult(adult);
            movie.setOverview(overview);
            movie.setRelease_date(realese_date);
            movie.setTitle(title);
            movie.setOriginal_title(original_title);
            movie.setBackdrop_path(backdrop_path);
            movie.setVote_average(vote_average);

            long codMovie = movieDAO.inserir(movie);

            if(codMovie > 0){
                sucesso = true;
            }
            else {
                sucesso = false;
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return sucesso;

    }

    private boolean deletarCadastroBanco(String id){
        movieDAO = new MovieDAO(this);
        boolean sucesso = false;
        try {
           sucesso = movieDAO.deletar(movie,id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sucesso;

    }


}
