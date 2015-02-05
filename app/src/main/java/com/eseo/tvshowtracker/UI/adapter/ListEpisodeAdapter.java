package com.eseo.tvshowtracker.UI.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.managers.TVShowManager;
import com.eseo.tvshowtracker.model.Episode;
import com.eseo.tvshowtracker.model.Season;
import com.eseo.tvshowtracker.model.TvShow;
import com.squareup.picasso.Picasso;

/**
 * Created by Damien on 02/02/2015.
 */
public class ListEpisodeAdapter extends ArrayAdapter<Episode> {

    private final Context context;
    private Season season;

    private static class ViewHolder {
        TextView episodeName;
        TextView airDate;
    }

    public ListEpisodeAdapter(Context context, Season season) {
        super(context,0, season.getEpisodes());
        this.season = season;
        this.context = context ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.detailedActivity_episode_row, parent, false);

            viewHolder.episodeName = (TextView) convertView.findViewById(R.id.episode_name);
            viewHolder.airDate = (TextView) convertView.findViewById(R.id.next_episode);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.episodeName.setText("Episode "+getItem(position).getName());
        viewHolder.airDate.setText(getItem(position).getAir_date());



        return convertView;

    }
}
