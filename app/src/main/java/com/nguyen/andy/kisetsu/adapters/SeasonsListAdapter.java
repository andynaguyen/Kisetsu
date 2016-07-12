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
    // Id's for drawable resources (icons)
    private static final int FALL_ICON_ID = R.drawable.fall_blue;
    private static final int SUMMER_ICON_ID = R.drawable.summer_blue;
    private static final int SPRING_ICON_ID = R.drawable.spring_blue;
    private static final int WINTER_ICON_ID = R.drawable.winter_blue;
    private static final int PLACEHOLDER_ICON_ID = R.drawable.base_icon;

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
                Drawable summerIcon = ResourcesCompat.getDrawable(iconResources, SUMMER_ICON_ID, null);
                holder.iconView.setImageDrawable(summerIcon);
                break;
            case SeasonItem.FALL:
                Drawable fallIcon = ResourcesCompat.getDrawable(iconResources, FALL_ICON_ID, null);
                holder.iconView.setImageDrawable(fallIcon);
                break;
            case SeasonItem.WINTER:
                Drawable winterIcon = ResourcesCompat.getDrawable(iconResources, WINTER_ICON_ID, null);
                holder.iconView.setImageDrawable(winterIcon);
                break;
            case SeasonItem.SPRING:
                Drawable springIcon = ResourcesCompat.getDrawable(iconResources, SPRING_ICON_ID, null);
                holder.iconView.setImageDrawable(springIcon);
                break;
            default:
                Drawable placeholderIcon = ResourcesCompat.getDrawable(iconResources, PLACEHOLDER_ICON_ID, null);
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