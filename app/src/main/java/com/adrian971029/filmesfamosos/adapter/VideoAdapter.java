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
import com.adrian971029.filmesfamosos.model.Video;

import java.util.List;

public class VideoAdapter extends ArrayAdapter<Video> {

    public VideoAdapter(Context context, List<Video> objects) {
        super(context,0, objects);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Video video = getItem(position);

        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_video,null);
            holder = new ViewHolder();
            holder.tituloVideo = (TextView) convertView.findViewById(R.id.tv_tituloVideo);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tituloVideo.setText(video.getName().toString());

        holder.tituloVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return convertView;

    }

    static class ViewHolder{
        TextView tituloVideo;
    }

}
