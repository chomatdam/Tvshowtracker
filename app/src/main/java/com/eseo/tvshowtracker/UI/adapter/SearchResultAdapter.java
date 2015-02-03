package com.eseo.tvshowtracker.UI.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.model.TvShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Fat on 02/02/2015.
 */
public class SearchResultAdapter extends ArrayAdapter<TvShow> {

    private final Context context;
    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w500/" ;

    private static class ViewHolder {
        ImageView poster;
        TextView name;
    }

    public SearchResultAdapter(Context context, ArrayList<TvShow> tvSeries) {
        super(context,0, tvSeries);
        this.context = context ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TvShow tvShow = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.search_result_row, parent, false);

            viewHolder.poster = (ImageView) convertView.findViewById(R.id.search_result_poster_image_view);
            viewHolder.name = (TextView) convertView.findViewById(R.id.search_result_name_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(tvShow.getName());


        Picasso.with(context)
                .load(IMAGE_URL+tvShow.getPoster_url())
                .error(R.drawable.tv_icon)
                .placeholder(R.drawable.tv_icon)
                .into(viewHolder.poster);


        return convertView;

    }
}
