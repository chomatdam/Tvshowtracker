package com.eseo.tvshowtracker.UI.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.model.Episode;
import com.eseo.tvshowtracker.model.Season;

/**
 * Created by Damien on 02/02/2015.
 */
public class EpisodeListAdapter extends ArrayAdapter<Episode> {

    private final Context context;

    private static class ViewHolder {
        TextView episodeName;
        TextView airDate;
        CheckBox seen ;
    }

    public EpisodeListAdapter(Context context, Season season) {
        super(context,0, season.getEpisodes());
        this.context = context ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_episode, parent, false);

            viewHolder.episodeName = (TextView) convertView.findViewById(R.id.episode_name);
            viewHolder.airDate = (TextView) convertView.findViewById(R.id.next_episode);
            viewHolder.seen = (CheckBox) convertView.findViewById(R.id.episode_seen_checkbox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Episode episode = getItem(position);
        viewHolder.episodeName.setText("Episode "+String.valueOf(position+1)+": "+episode.getName());
        viewHolder.airDate.setText(getItem(position).getAir_date());
        viewHolder.seen.setOnCheckedChangeListener(new MyCheckBoxListener(episode,viewHolder));
        viewHolder.seen.setChecked(episode.isSeen()==1?true:false);

        return convertView;

    }


    private class MyCheckBoxListener implements CompoundButton.OnCheckedChangeListener {

        private Episode episode ;
        private ViewHolder viewHolder ;

        public MyCheckBoxListener(Episode episode, ViewHolder viewHolder ){
            this.episode = episode ;
            this.viewHolder = viewHolder ;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                episode.setSeen(1);
            }
            else {
                episode.setSeen(0);
            }
        }
    }


}
