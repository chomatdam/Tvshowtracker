package com.eseo.tvshowtracker.UI.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.UI.listeners.OnSeasonSelectedListener;
import com.eseo.tvshowtracker.model.Season;
import com.eseo.tvshowtracker.model.TvShow;

import java.util.ArrayList;

/**
 * Created by Damien on 02/02/2015.
 */
public class SeasonListAdapter extends BaseAdapter {

    private static final int HEADER = 0 ;
    private static final int ITEM_OVERVIEW = 1 ;
    private static final int ITEM_SEASON = 2 ;

    private final Context context;
    private int nbSeasons ;
    private OnSeasonSelectedListener listener ;
    private ArrayList mData = new ArrayList();

    public SeasonListAdapter(Context context, TvShow tvShow,OnSeasonSelectedListener listener) {
        super();

        this.mData.add(new String("Seasons"));
        this.mData.addAll(tvShow.getSeasons());
        this.mData.add(new String("Overviews"));
        this.mData.add(tvShow.getOverview());
        this.nbSeasons = tvShow.getSeasons().size();
        this.context = context ;
        this.listener = listener ;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        int itemType ;
        if(position == 0 || position == nbSeasons+1){
            itemType = HEADER ;
        }
        else if(position<=nbSeasons){
            itemType = ITEM_SEASON ;
        }
        else {
            itemType = ITEM_OVERVIEW ;
        }
        return itemType ;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        switch(type) {
            case HEADER : {
                HeaderViewHolder viewHolder;
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.header_section_season_list, parent, false);
                    viewHolder = new HeaderViewHolder();
                    viewHolder.header = (TextView) convertView.findViewById(R.id.title_header_textView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (HeaderViewHolder) convertView.getTag();
                }
                viewHolder.header.setText((String) mData.get(position));
                break;
            }
            case ITEM_OVERVIEW : {
                OverviewViewHolder viewHolder;
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.row_overview, parent, false);
                    viewHolder = new OverviewViewHolder();
                    viewHolder.text = (TextView) convertView.findViewById(R.id.overview_textView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (OverviewViewHolder) convertView.getTag();
                }
                viewHolder.text.setText((String) mData.get(position));
                break;
            }
            case ITEM_SEASON : {
                final SeasonViewHolder viewHolder;
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.row_season, parent, false);
                    viewHolder = new SeasonViewHolder();
                    viewHolder.seasonName = (TextView) convertView.findViewById(R.id.season_name_textView);
                    viewHolder.seen = (CheckBox) convertView.findViewById(R.id.season_seen_checkbox);
                    viewHolder.numberEpisodesProgressBar = (ProgressBar) convertView.findViewById(R.id.nb_episodes_progressBar);
                    viewHolder.numberEpisodesTextView = (TextView) convertView.findViewById(R.id.nb_episodes_textView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (SeasonViewHolder) convertView.getTag();
                }
                Season season = (Season)mData.get(position) ;
                int nbSeenEpisodes = season.getNumberSeenEpisodes();
                final int nbEpisodes = season.getEpisodes().size();

                viewHolder.seasonName.setText("SEASON "+season.getSeason_number());
                viewHolder.seen.setOnCheckedChangeListener(new MyCheckBoxListener(season,viewHolder));
                viewHolder.seen.setChecked(nbSeenEpisodes==nbEpisodes ? true : false);
                viewHolder.numberEpisodesProgressBar.setProgress((int)((double)nbSeenEpisodes/(double)nbEpisodes * 100));
                viewHolder.numberEpisodesTextView.setText(nbSeenEpisodes + "/" + nbEpisodes);


                convertView.setOnClickListener(new MyItemListListener(position));
                break;
            }
        }
        return convertView;
    }
    private static class SeasonViewHolder {
        CheckBox seen ;
        TextView seasonName;
        TextView numberEpisodesTextView;
        ProgressBar numberEpisodesProgressBar ;
    }
    private static class HeaderViewHolder {
        TextView header;
    }
    private static class OverviewViewHolder {
        TextView text;
    }

    private class MyCheckBoxListener implements CompoundButton.OnCheckedChangeListener {

        private Season season ;
        private SeasonViewHolder viewHolder ;

        public MyCheckBoxListener(Season season, SeasonViewHolder viewHolder ){
            this.season = season ;
            this.viewHolder = viewHolder ;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int nbEpisodes = season.getEpisodes().size();
            if (isChecked) {
                viewHolder.numberEpisodesProgressBar.setProgress(100);
                viewHolder.numberEpisodesTextView.setText(nbEpisodes + "/" + nbEpisodes);
                season.updateSeenEpisodes(1);
            } else {
                viewHolder.numberEpisodesProgressBar.setProgress(0);
                viewHolder.numberEpisodesTextView.setText(0 + "/" + nbEpisodes);
                season.updateSeenEpisodes(0);
            }
        }
    }

    private class MyItemListListener implements View.OnClickListener{

        private int position ;

        public MyItemListListener(int position){
            this.position = position ;
        }

        @Override
        public void onClick(View v) {
            listener.onSeasonSelected(position-1);
        }
    }



}

