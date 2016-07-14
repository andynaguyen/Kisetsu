package com.nguyen.andy.kisetsu.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.nguyen.andy.kisetsu.AnimeItem;
import com.nguyen.andy.kisetsu.R;

/**
 * Created by Andy on 7/13/2016.
 */
public class AnimeListAdapter extends BaseAdapter{
    ArrayList<AnimeItem> animeList;
    private LayoutInflater layoutInflater;

    public AnimeListAdapter(Context context, ArrayList<AnimeItem> animeData) {
        this.animeList = animeData;
        layoutInflater = LayoutInflater.from(context);
    }

    /*
     * Returns total number of row elements in the GridView (# of anime)
     */
    @Override
    public int getCount() {
        return animeList.size();
    }

    /*
     * Returns AnimeItem representing data at each row.
     */
    @Override
    public Object getItem(int position) {
        return animeList.get(position);
    }

    /*
     * Returns unique numeric id representing each grid item in the GridView.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * Returns a view instance that representing a single grid item in the GridView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.anime_layout, null);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.anime_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleView.setText(animeList.get(position).getTitle());
        holder.titleView.setTextColor(Color.BLACK);  // change this when the picture is loading properly

        return convertView;
    }

    /*
     * Class to hold all the views in the row. Static so we can check if an instance of it exists already.
     */
    static class ViewHolder {
        TextView titleView;
    }
}
