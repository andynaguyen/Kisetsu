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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.nguyen.andy.kisetsu.adapters.AnimeListAdapter;
import com.nguyen.andy.kisetsu.parsers.AnimeCatalogParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnimeCatalogActivity extends AppCompatActivity {
    // constants
    private static final String MAL_PREFIX_URL = "http://myanimelist.net/anime/season/";
    private static final String KISETSU_PREFIX = "http://kisetsu.pythonanywhere.com/season/";
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

        String url = buildKisetsuURL(season, year);

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

    /**
     * Builds the proper Kisetsu API endpoint given a season and year
     * @param season the given season
     * @param year the given year
     * @return the endpoint
     */
    private String buildKisetsuURL(String season, int year) {
        StringBuilder url = new StringBuilder();
        url.append(KISETSU_PREFIX);
        url.append(year);
        url.append('/');
        url.append(season.toLowerCase());

        return url.toString();
    }

    // AsyncTask dedicated to parsing the HTML
    private class ParseURLTask extends AsyncTask<String, Void, String> {
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
        protected String doInBackground(String... params) {
            String jsonResponse = "";
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder sb = new StringBuilder();
                String line = "";

                while((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                jsonResponse =  sb.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            ArrayList<AnimeItem> animeItems = new ArrayList<AnimeItem>();
            try {
                JSONArray animeList = new JSONArray(jsonResponse);
                for (int i = 0; i < animeList.length(); i++) {
                    JSONObject anime = animeList.getJSONObject(i);
                    String title = anime.getString("title");
                    String imgUrl = anime.getString("img_url");
                    String malUrl = anime.getString("mal_url");

                    animeItems.add(new AnimeItem(title, imgUrl, malUrl));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*final ArrayList<AnimeItem> animeItems = parser.parseAnimeList();*/

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

            progessDialog.dismiss();
        }
    }
}
