package com.eseo.tvshowtracker.managers;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.eseo.tvshowtracker.model.Season;
import com.eseo.tvshowtracker.model.TvShow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Fat on 04/02/2015.
 */
public class GetTvShowThread extends Thread {

    public static final int TV_SHOW_ANSWER = 1 ;

    private long mTvShowId ;
    private Handler mResultHandler ;
    private Context context;

    public GetTvShowThread(Context c, long tvShowId, Handler resultHandler){
        this.mTvShowId = tvShowId ;
        this.mResultHandler = resultHandler ;
        context = c;
    }

    @Override
    public void run() {
        SQLiteManager mSqLiteManager = new SQLiteManager(context);
        RESTService service = TVShowManager.getInstance().getService();
        TvShow tvShow = service.getDataTVShow(mTvShowId);

        int i = 0 ;
        for(Season season : tvShow.getSeasons()){
            season = service.getDataSeason(mTvShowId,season.getSeason_number());
            tvShow.getSeasons().set(i++,season);
        }
        tvShow.setNextEpisode(getLastestAirDate(tvShow));
        mSqLiteManager.createTvShow(tvShow);

        Message msg = mResultHandler.obtainMessage(TV_SHOW_ANSWER,tvShow);
        mResultHandler.sendMessage(msg);
    }

    public String getLastestAirDate(TvShow tvShow){
        String result = "Next air date not known";
        try {
        int lastSeason = tvShow.getSeasons().size()-1;
        int lastEpisode = tvShow.getSeasons().get(lastSeason).getEpisodes().size()-1;
        String lastAirDate = tvShow.getSeasons().get(lastSeason).getEpisodes().get(lastEpisode).getAir_date();

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(lastAirDate);
        if(date.compareTo(currentDate)>=0) {
            result = lastAirDate;
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

}
