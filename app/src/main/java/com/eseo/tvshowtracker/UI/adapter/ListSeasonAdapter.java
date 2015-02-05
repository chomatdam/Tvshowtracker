package com.eseo.tvshowtracker.UI.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.managers.TVShowManager;
import com.eseo.tvshowtracker.model.Season;
import com.eseo.tvshowtracker.model.TvShow;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Damien on 02/02/2015.
 */
public class ListSeasonAdapter extends ArrayAdapter<Season> {

    private final Context context;
    private TvShow tvshow;

    private static class ViewHolder {
        ImageView poster;
        TextView name;
    }

    public ListSeasonAdapter(Context context, TvShow tvShow) {
        super(context,0, tvShow.getSeasons());
        this.tvshow = tvShow;
        this.context = context ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.tv_show_row, parent, false);

            viewHolder.poster = (ImageView) convertView.findViewById(R.id.tvshow_poster_image_view);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvshow_name_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText("Season "+getItem(position).getSeason_number());

        Picasso.with(context)
                .load(TVShowManager.IMAGE_URL+getItem(position).getPoster_path())
                .error(R.drawable.tv_icon)
                .placeholder(R.drawable.tv_icon)
                .into(viewHolder.poster);


        return convertView;

    }
}
