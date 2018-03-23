package com.adrian971029.filmesfamosos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.adrian971029.filmesfamosos.R;
import com.adrian971029.filmesfamosos.model.Movie;
import com.adrian971029.filmesfamosos.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Adrian on 23/03/2018.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Context context, List<Movie> objects) {
        super(context,0, objects);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);

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

        return convertView;

    }


    static class ViewHolder{
        ImageView imgCapa;
    }

}
