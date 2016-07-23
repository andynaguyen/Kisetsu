package com.nguyen.andy.kisetsu.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.nguyen.andy.kisetsu.SeasonItem;
import com.nguyen.andy.kisetsu.R;
/**
 * Created by Andy on 7/7/2016.
 */
public class SeasonsListAdapter extends BaseAdapter {
    // Id's for drawable resources (icons)
    private static final int FALL_ICON_ID;
    private static final int SUMMER_ICON_ID;
    private static final int SPRING_ICON_ID;
    private static final int WINTER_ICON_ID;
    private static final int PLACEHOLDER_ICON_ID = R.drawable.base_icon;

    static {
        int num = 0 ;

        switch (num) {
            case 0:
                FALL_ICON_ID = R.drawable.fall_match;
                SUMMER_ICON_ID = R.drawable.summer_match;
                WINTER_ICON_ID = R.drawable.winter_match;
                SPRING_ICON_ID = R.drawable.spring_match;
                break;
            case 1:
                FALL_ICON_ID = R.drawable.fall_orange;
                SUMMER_ICON_ID = R.drawable.summer_yellow;
                WINTER_ICON_ID = R.drawable.winter_blue;
                SPRING_ICON_ID = R.drawable.spring_green;
                break;
            default:
                FALL_ICON_ID = R.drawable.fall_blu;
                SUMMER_ICON_ID = R.drawable.summer_blu;
                WINTER_ICON_ID = R.drawable.winter_blu;
                SPRING_ICON_ID = R.drawable.string_blu;
                break;
        }
    }

    ArrayList<SeasonItem> seasonList;
    private LayoutInflater layoutInflater;
    private Context context;

    public SeasonsListAdapter(Context context, ArrayList<SeasonItem> seasonData) {
        this.seasonList = seasonData;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    /*
     * Returns total number of row elements in the ListView (# of seasons)
     */
    @Override
    public int getCount() {
        return seasonList.size();
    }

    /*
     * Returns SeasonItem representing data at each row.
     */
    @Override
    public Object getItem(int position) {
        return seasonList.get(position);
    }

    /*
     * Returns unique numeric id representing each row item in the ListView.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * Returns a view instance that representing a single row in the ListView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.season_layout, null);
            holder = new ViewHolder();
            holder.iconView = (ImageView) convertView.findViewById(R.id.season_icon);
            holder.nameView = (TextView) convertView.findViewById(R.id.season_title);
            holder.timeView = (TextView) convertView.findViewById(R.id.season_timeframe);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameView.setText(seasonList.get(position).getName());
        holder.timeView.setText(seasonList.get(position).getTimeframe());

        // Get the imageView set to the appropriate season icon
        switch (seasonList.get(position).getSeason()) {
            case SeasonItem.SUMMER:
                Picasso.with(context)
                        .load(SUMMER_ICON_ID)
                        .into(holder.iconView);
                break;
            case SeasonItem.FALL:
                Picasso.with(context)
                        .load(FALL_ICON_ID)
                        .into(holder.iconView);
                break;
            case SeasonItem.WINTER:
                Picasso.with(context)
                        .load(WINTER_ICON_ID)
                        .into(holder.iconView);
                break;
            case SeasonItem.SPRING:
                Picasso.with(context)
                        .load(SPRING_ICON_ID)
                        .into(holder.iconView);
                break;
            default:
                Picasso.with(context)
                        .load(PLACEHOLDER_ICON_ID)
                        .into(holder.iconView);
                break;
        }

        return convertView;
    }

    /*
     * Class to hold all the views in the row. Static so we can check if an instance of it exists already.
     */
    static class ViewHolder {
        ImageView iconView;
        TextView nameView;
        TextView timeView;
    }
}