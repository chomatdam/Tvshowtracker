package com.eseo.tvshowtracker.managers;

import android.os.Handler;
import android.os.Message;

import com.eseo.tvshowtracker.model.Season;
import com.eseo.tvshowtracker.model.TvShow;

/**
 * Created by Fat on 04/02/2015.
 */
public class GetTvShowThread extends Thread {

    public static final int TV_SHOW_ANSWER = 1 ;

    private long mTvShowId ;
    private Handler mResultHandler ;

    public GetTvShowThread(long tvShowId, Handler resultHandler){
        this.mTvShowId = tvShowId ;
        this.mResultHandler = resultHandler ;
    }

    @Override
    public void run() {
        RESTService service = TVShowManager.getInstance().getService();
        TvShow tvShow = service.getDataTVShow(mTvShowId);

        int i = 0 ;
        for(Season season : tvShow.getSeasons()){
            season = service.getDataSeason(mTvShowId,season.getSeason_number());
            tvShow.getSeasons().set(i++,season);
        }

        Message msg = mResultHandler.obtainMessage(TV_SHOW_ANSWER,tvShow);
        mResultHandler.sendMessage(msg);
    }
}
