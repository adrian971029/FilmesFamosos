package com.adrian971029.filmesfamosos.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.adrian971029.filmesfamosos.R;
import com.adrian971029.filmesfamosos.activity.DetailsActivity;
import com.adrian971029.filmesfamosos.model.Movie;
import com.adrian971029.filmesfamosos.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Adrian on 23/03/2018.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    private List<Movie> mListMovie;
    private static final String MOVIE = "movie";

    @Override
    public int getCount() {
        return mListMovie.size();
    }

    public MovieAdapter(Context context, List<Movie> objects) {
        super(context,0, objects);
        mListMovie = objects;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Movie movie = getItem(position);

        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie_grid,null);
            holder = new ViewHolder();
            holder.imgCapa = (ImageView)convertView.findViewById(R.id.imgMovie);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        Picasso.with(getContext()).load(Constants.GET_IMAGES + movie.getPoster_path()).into(holder.imgCapa);

        holder.imgCapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamaDetailsActivity(movie);
            }
        });



        return convertView;

    }

    private void llamaDetailsActivity(Movie movie){
        Intent i = new Intent(getContext(), DetailsActivity.class);
        i.putExtra(MOVIE,movie);
        getContext().startActivity(i);
    }


    static class ViewHolder{
        ImageView imgCapa;
    }

}
