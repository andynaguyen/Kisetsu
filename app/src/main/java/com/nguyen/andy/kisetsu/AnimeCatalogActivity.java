package com.nguyen.andy.kisetsu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import com.nguyen.andy.kisetsu.adapters.AnimeListAdapter;
import com.nguyen.andy.kisetsu.parsers.AnimeCatalogParser;

public class AnimeCatalogActivity extends AppCompatActivity {
    // constants
    private static final String MAL_PREFIX_URL = "http://myanimelist.net/anime/season/";
    private static final String INTERNET_ERROR_MESSAGE = "No Internet Connection";

    // fields
    ProgressDialog progessDialog;
    String season;
    int year;
    SearchView searchView;
    GridView animeGridView;
    AnimeListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_catalog);

        // unpack variables from intent
        Bundle bundle = getIntent().getExtras();
        season = bundle.getString("Season");
        year = bundle.getInt("Year");

        // ex: "SUMMER" -> "Summer" + year
        String title =
                season.substring(0,1).toUpperCase() + season.substring(1).toLowerCase() + " " + year;
        setTitle(title);

        Toolbar toolbar = (Toolbar) findViewById(R.id.anime_toolbar);
        setSupportActionBar(toolbar);

        String url = buildURL(season, year);

        // if connected to internet, start web scraping.
        // else, make toast
        if (ConnectivityCheck.isConnectedToInternet(getApplicationContext())) {
            new ParseURLTask().execute(url);
        } else{
            Toast.makeText(this, INTERNET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.anime_catalog_actions, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        android.support.v7.widget.SearchView sv = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        sv.setQueryHint("Unlimited Search Works");

        sv.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.resetData();
                adapter.getFilter().filter(newText.toString());
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Builds the proper MAL url given a season and year.
     *  EX: Given "Summer" and "2013", returns "http://myanimelist.net/anime/season/2013/summer"
     * @param season the given season
     * @param year the given year
     * @return the MAL url
     */
    private String buildURL(String season, int year) {
        // URL FORMAT: PREFIX + SEASON (in lowercase) + '/' + YEAR
        StringBuilder url = new StringBuilder();
        url.append(MAL_PREFIX_URL);
        url.append(year);
        url.append('/');
        url.append(season.toLowerCase());

        return url.toString();
    }

    // AsyncTask dedicated to parsing the HTML
    private class ParseURLTask extends AsyncTask<String, Void, AnimeCatalogParser> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Start progess dialog
            progessDialog = new ProgressDialog(AnimeCatalogActivity.this);
            progessDialog.setTitle("Fetching data");
            progessDialog.setMessage("Loading...");
            progessDialog.setIndeterminate(false);
            progessDialog.show();
        }

        @Override
        protected AnimeCatalogParser doInBackground(String... params) {
            return new AnimeCatalogParser(params[0]);
        }

        @Override
        protected void onPostExecute(AnimeCatalogParser parser) {
            final ArrayList<AnimeItem> animeItems = parser.parseAnimeList();

            // populate gridview with anime series
            animeGridView = (GridView) findViewById(R.id.anime_catalog);
            adapter = new AnimeListAdapter(getApplicationContext(), animeItems);
            animeGridView.setAdapter(adapter);
            animeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AnimeItem animeData = (AnimeItem) animeGridView.getItemAtPosition(position);

                    Intent intent = new Intent(getApplicationContext(), AnimeDetailActivity.class);
                    intent.putExtra("MalUrl", animeData.getMalURL());
                    intent.putExtra("ImgUrl", animeData.getImageUrl());
                    intent.putExtra("Title", animeData.getTitle());
                    intent.putExtra("SeasonFrom", season);
                    intent.putExtra("YearFrom", year);
                    startActivity(intent);
                }
            });

            /*/ initialize SearchView
            searchView = (SearchView) findViewById(R.id.search);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.resetData();
                    adapter.getFilter().filter(newText.toString());
                    return false;
                }
            });*/

            progessDialog.dismiss();
        }
    }
}
