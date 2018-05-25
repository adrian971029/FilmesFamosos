package com.adrian971029.filmesfamosos.activity;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.adrian971029.filmesfamosos.model.Review;
import com.adrian971029.filmesfamosos.model.Video;
import com.adrian971029.filmesfamosos.utils.Constants;
import com.adrian971029.filmesfamosos.utils.network.HttpConnection;
import com.adrian971029.filmesfamosos.utils.network.ReviewHttp;
import com.adrian971029.filmesfamosos.utils.network.VideoHttp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

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
    private VideoTask mVideoTask;
    private ReviewTask mReviewTask;
    private ListView mListViewVideo;
    private ListView mListViewReview;
    private ProgressBar mProgressBarVideo;
    private ProgressBar mProgressBarReview;
    private TextView mTextMensagemVideo;
    private TextView mTextMensagemReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        inicializandoComponentes();
        recibiendoValores();
        mostrandoValores();
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
        if (adult){
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
        if(mVideoTask == null){
            if(HttpConnection.temConexao(this)){
                iniciarDownloadVideo();
            }
            else{
                mTextMensagemVideo.setText(R.string.sem_conexao);
            }
        }
        else if(mVideoTask.getStatus() == AsyncTask.Status.RUNNING){
            exibirProgressVideo(true);
        }
    }

    private void crearLayoutReview(){
        if(mReview == null){
            mReview = new ArrayList<Review>();
        }
        mAdapterReview = new ReviewAdapter(getApplicationContext(),mReview);
        mListViewReview.setAdapter(mAdapterReview);
        if(mReviewTask == null){
            if(HttpConnection.temConexao(this)){
                iniciarDownloadReview();
            }
            else{
                mTextMensagemReview.setText(R.string.sem_conexao);
            }
        }
        else if(mReviewTask.getStatus() == AsyncTask.Status.RUNNING){
            exibirProgressReview(true);
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

    private void iniciarDownloadVideo(){
        if(mVideoTask == null || mVideoTask.getStatus() != AsyncTask.Status.RUNNING){
            mVideoTask = new VideoTask();
            mVideoTask.execute();
        }
    }

    private void iniciarDownloadReview(){
        if(mReviewTask == null || mReviewTask.getStatus() != AsyncTask.Status.RUNNING){
            mReviewTask = new ReviewTask();
            mReviewTask.execute();
        }
    }

    class VideoTask extends AsyncTask<Void,Void,List<Video>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgressVideo(true);
        }

        @Override
        protected List<Video> doInBackground(Void... voids) {
            return VideoHttp.carregarVideoJson(String.valueOf(id_movie));
        }

        @Override
        protected void onPostExecute(List<Video> videos) {
            super.onPostExecute(videos);
            exibirProgressVideo(false);
            if(videos != null){
                mVideo.clear();
                mVideo.addAll(videos);
                mAdapterVideo.notifyDataSetChanged();
            }
            else {
                mTextMensagemVideo.setText( R.string.falha_filmes_trailers);
            }
        }
    }

    class ReviewTask extends AsyncTask<Void,Void,List<Review>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgressReview(true);
        }

        @Override
        protected List<Review> doInBackground(Void...voids) {
            return ReviewHttp.carregarReviewJson(String.valueOf(id_movie));
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            super.onPostExecute(reviews);
            exibirProgressReview(false);
            if(reviews != null){
                mReview.clear();
                mReview.addAll(reviews);
                mAdapterReview.notifyDataSetChanged();
            }
            else {
                mTextMensagemReview.setText(R.string.falha_reviews);
            }
        }
    }

}
