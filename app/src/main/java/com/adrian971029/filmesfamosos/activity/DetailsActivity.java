package com.adrian971029.filmesfamosos.activity;

import android.content.SharedPreferences;
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
import com.adrian971029.filmesfamosos.data.ContentProviderAccess;
import com.adrian971029.filmesfamosos.io.ApiAdapter;
import com.adrian971029.filmesfamosos.io.ApiService;
import com.adrian971029.filmesfamosos.io.response.ReviewResponse;
import com.adrian971029.filmesfamosos.io.response.VideoResponse;
import com.adrian971029.filmesfamosos.model.Movie;
import com.adrian971029.filmesfamosos.model.Review;
import com.adrian971029.filmesfamosos.model.Video;
import com.adrian971029.filmesfamosos.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends BaseActivity {

    private static final String MOVIE = "movie";

    @BindView(R.id.imgPoster_Path) ImageView mPosterPath;
    @BindView(R.id.imgBackdrop_Path) ImageView mBackdropPath;
    @BindView(R.id.img_simfavorito) ImageView mFavoritoImageView;
    @BindView(R.id.img_naofavorito) ImageView mNaoFavoritoImageView;
    @BindView(R.id.tv_overview) TextView mOverview;
    @BindView(R.id.tv_title) TextView mTitle;
    @BindView(R.id.tv_originalTitle) TextView mOriginalTitle;
    @BindView(R.id.tv_releaseDate) TextView mReleaseDate;
    @BindView(R.id.tv_adult) TextView mAdult;
    @BindView(R.id.tv_voteAverage) TextView mVoteAverage;
    @BindView(R.id.lv_trailerVideo) ListView mListViewVideo;
    @BindView(R.id.lv_reviews) ListView mListViewReview;
    @BindView(R.id.progressBarVideo) ProgressBar mProgressBarVideo;
    @BindView(R.id.progressBarReviews) ProgressBar mProgressBarReview;
    @BindView(R.id.tv_aguardeVideo) TextView mTextMensagemVideo;
    @BindView(R.id.tv_aguardeReviews) TextView mTextMensagemReview;
    private String poster_path,backdrop_path,overview,title,original_title,realese_date;
    private long id_movie;
    private boolean adult;
    private float vote_average;
    private List<Video> mVideo;
    private List<Review> mReview;
    private ArrayAdapter<Video> mAdapterVideo;
    private ArrayAdapter<Review> mAdapterReview;
    private boolean controlFavorito;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        controlFavorito = false;

        ButterKnife.bind(this);

        movie = new Movie();
        recibiendoValores();
        mostrandoValores();
        exibirProgressVideo(true);
        exibirProgressReview(true);
        controlFavorito();
        crearLayoutTrailer();
        crearLayoutReview();

    }

    private void recibiendoValores(){
        movie = getIntent().getExtras().getParcelable(MOVIE);
        id_movie = movie.getId();
        poster_path = movie.getPoster_path();
        backdrop_path = movie.getBackdrop_path();
        overview = movie.getOverview();
        title = movie.getTitle();
        original_title = movie.getOriginal_title();
        realese_date = movie.getRelease_date();
        adult = movie.isAdult();
        vote_average = movie.getVote_average();
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

        if(temConexao(this)){
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

        if(temConexao(this)){
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
        inserirCadastroBanco();
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

    private void inserirCadastroBanco() {

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

            ContentProviderAccess.inserirContentProvider(this,movie);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean deletarCadastroBanco(String id){
        boolean sucesso = false;
        try {
           sucesso = ContentProviderAccess.deletar(movie,id,this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sucesso;
    }


}
