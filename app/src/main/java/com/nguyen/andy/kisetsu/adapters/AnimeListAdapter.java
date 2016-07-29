package com.nguyen.andy.kisetsu.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.nguyen.andy.kisetsu.AnimeItem;
import com.nguyen.andy.kisetsu.R;

/**
 * Adapter. Loads AnimeItems form the list to the GridView.
 */
public class AnimeListAdapter extends BaseAdapter implements Filterable {
    ArrayList<AnimeItem> animeList;
    ArrayList<AnimeItem> originalAnimeList;
    private LayoutInflater layoutInflater;
    private AnimeFilter animeFilter;
    Context context;

    public AnimeListAdapter(Context context, ArrayList<AnimeItem> animeData) {
        this.animeList = animeData;
        this.originalAnimeList = animeData;
        this.context = context;
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
            holder.imgView = (ImageView) convertView.findViewById(R.id.catalog_thumbnail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleView.setText(animeList.get(position).getTitle());

        /*DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpheight = displayMetrics.heightPixels / displayMetrics.density;
        float dpwidth  = displayMetrics.widthPixels  / displayMetrics.density;

        double imgWidth = (dpwidth / 2) - 16 - 3;
        double imgHeight = imgWidth * 1.4;

        Log.d("dp", "height: " + imgHeight + "dp width: " + imgWidth + "dp");

        holder.imgView.getLayoutParams().height = (int) Math.round(imgHeight);
*/
        Picasso.with(context)
                .load(animeList.get(position).getImageUrl())
                .fit()
                .into(holder.imgView);

        return convertView;
    }

    /*
     * Class to hold all the views in the row. Static so we can check if an instance of it exists already.
     */
    static class ViewHolder {
        TextView titleView;
        ImageView imgView;
    }

    public void resetData() {
        animeList = originalAnimeList;
    }

    @Override
    public Filter getFilter() {
        if (animeFilter == null) {
            animeFilter = new AnimeFilter();
        }
        return animeFilter;
    }

    private class AnimeFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = animeList;
                results.count = animeList.size();

                return results;
            }

            ArrayList<AnimeItem> newList = new ArrayList<AnimeItem>();

            for (AnimeItem anime : animeList) {
                if (anime.getTitle().toLowerCase().contains(query)) {
                    newList.add(anime);
                }
            }

            results.values = newList;
            results.count = newList.size();

            for (AnimeItem item : newList)
                Log.d("filterList", item.getTitle());

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            animeList = (ArrayList<AnimeItem>) results.values;
            notifyDataSetChanged();
        }
    }
}
