package com.adrian971029.filmesfamosos.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.adrian971029.filmesfamosos.R;
import com.adrian971029.filmesfamosos.utils.Constants;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private ImageView mPosterPath,mBackdropPath;
    private TextView mOverview,mTitle,mOriginalTitle,mReleaseDate,mAdult,mVoteAverage;
    private String poster_path,backdrop_path,overview,title,original_title,realese_date;
    private boolean adult;
    private float vote_average;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        inicializandoComponentes();
        recibiendoValores();
        mostrandoValores();

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
    }

    private void recibiendoValores(){
        Intent intent = getIntent();
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

}
