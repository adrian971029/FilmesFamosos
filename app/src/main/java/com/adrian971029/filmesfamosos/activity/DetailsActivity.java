package com.adrian971029.filmesfamosos.activity;

import android.content.Intent;
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
import com.adrian971029.filmesfamosos.io.ApiAdapter;
import com.adrian971029.filmesfamosos.io.ApiService;
import com.adrian971029.filmesfamosos.io.response.ReviewResponse;
import com.adrian971029.filmesfamosos.io.response.VideoResponse;
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
        exibirProgressVideo(true);
        exibirProgressReview(true);
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

}
