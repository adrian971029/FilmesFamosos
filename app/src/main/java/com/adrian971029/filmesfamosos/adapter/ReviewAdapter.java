package com.adrian971029.filmesfamosos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adrian971029.filmesfamosos.R;
import com.adrian971029.filmesfamosos.model.Review;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Context context, List<Review> objects) {
        super(context,0, objects);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Review review = getItem(position);

        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_review,null);
            holder = new ViewHolder();
            holder.autorReview = convertView.findViewById(R.id.tv_authorReview);
            holder.contentReview = convertView.findViewById(R.id.tv_contentReview);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.autorReview.setText(review.getAuthor().toString());
        holder.contentReview.setText(review.getContent().toString());

        return convertView;

    }

    static class ViewHolder{
        TextView autorReview;
        TextView contentReview;
    }

}
