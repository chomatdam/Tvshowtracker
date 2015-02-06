package com.eseo.tvshowtracker.UI.fragments;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.UI.adapter.SeasonListAdapter;
import com.eseo.tvshowtracker.UI.listeners.OnSeasonSelectedListener;
import com.eseo.tvshowtracker.managers.TVShowManager;
import com.eseo.tvshowtracker.model.TvShow;
import com.squareup.picasso.Picasso;

/**
 * Created by Damien on 05/02/2015.
 */
public class SeasonListFragment extends ListFragment implements OnSeasonSelectedListener{

    private TvShow tvshow;
    private SeasonListAdapter mAdapter ;

    public static SeasonListFragment newInstance(TvShow tvshow) {
        SeasonListFragment fragment = new SeasonListFragment();
        fragment.tvshow = tvshow;
        return fragment;
    }

    public SeasonListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new SeasonListAdapter(getActivity(),tvshow,this);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episode_list,container,false);
        ListView listView = (ListView)view.findViewById(android.R.id.list);

        View header = inflater.inflate(R.layout.header_season_list, listView, false);
        ImageView headerImageView = (ImageView)header.findViewById(R.id.top_header_imageView);
        Picasso.with(getActivity())
                .load(TVShowManager.IMAGE_URL+tvshow.getPoster_url())
                .error(R.drawable.gray_background)
                .placeholder(R.drawable.gray_background)
                .fit()
                .centerCrop()
                .into(headerImageView);

        listView.addHeaderView(header);

        return view;
    }


    @Override
    public void onSeasonSelected(int position) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.placeholder,EpisodeListFragment.newInstance(tvshow.getSeasons().get(position))).addToBackStack("EpisodeList").commit();
    }
}
