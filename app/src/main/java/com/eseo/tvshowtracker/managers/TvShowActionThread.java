package com.eseo.tvshowtracker.managers;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.eseo.tvshowtracker.model.Season;
import com.eseo.tvshowtracker.model.TvShow;

/**
 * Created by Damien on 2/4/2015.
 */
public class TvShowActionThread extends Thread {

    public static final int TV_SHOW_ANSWER = 1 ;

    private long mTvShowId ;
    private Context context;
    private Handler mResultHandler ;

    public TvShowActionThread(Context c, long tvShowId, Handler resultHandler){
        this.mTvShowId = tvShowId ;
        this.mResultHandler = resultHandler ;
        this.context = c;
    }

    @Override
    public void run() {
        //TODO(thread): database actions
    }
}
