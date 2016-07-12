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

import java.util.ArrayList;

import com.nguyen.andy.kisetsu.SeasonItem;
import com.nguyen.andy.kisetsu.R;
/**
 * Created by Andy on 7/7/2016.
 */
public class SeasonsListAdapter extends BaseAdapter {
    ArrayList<SeasonItem> seasonList;
    private LayoutInflater layoutInflater;

    public SeasonsListAdapter(Context context, ArrayList<SeasonItem> seasonData) {
        this.seasonList = seasonData;
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
     * Returns unique int id representing each row item in the ListView.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * Returns a view instance that representing a single row in the ListView.
     */
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

        // An intermediate variable
        Resources iconResources = holder.iconView.getContext().getResources();

        // Get the imageView set to the appropriate season icon
        switch (seasonList.get(position).getSeason()) {
            case SeasonItem.SUMMER:
                Drawable summerIcon = ResourcesCompat.getDrawable(iconResources, R.drawable.summer_icon, null);
                holder.iconView.setImageDrawable(summerIcon);
                //Log.d("season", "Summer");
                break;
            case SeasonItem.FALL:
                Drawable fallIcon = ResourcesCompat.getDrawable(iconResources, R.drawable.fall_icon, null);
                holder.iconView.setImageDrawable(fallIcon);
                //Log.d("season", "Fall");
                break;
            case SeasonItem.WINTER:
                Drawable winterIcon = ResourcesCompat.getDrawable(iconResources, R.drawable.winter_icon, null);
                holder.iconView.setImageDrawable(winterIcon);
                //Log.d("season", "Winter");
                break;
            case SeasonItem.SPRING:
                Drawable springIcon = ResourcesCompat.getDrawable(iconResources, R.drawable.spring_icon, null);
                holder.iconView.setImageDrawable(springIcon);
                //Log.d("season", "Spring");
                break;
            default:
                //Log.d("season","WHY IS THIS NULL");
                Drawable placeholderIcon = ResourcesCompat.getDrawable(iconResources, R.drawable.spring_icon, null);
                holder.iconView.setImageDrawable(placeholderIcon);
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