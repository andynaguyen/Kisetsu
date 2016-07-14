package com.nguyen.andy.kisetsu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import com.nguyen.andy.kisetsu.adapters.AnimeListAdapter;

public class AnimeCatalogActivity extends AppCompatActivity {
    private static final String MAL_PREFIX_URL = "http://myanimelist.net/anime/season/";
    //ArrayList<String> titles;
    ProgressDialog progessDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_catalog);

        Bundle bundle = getIntent().getExtras();
        String season = bundle.getString("Season");
        int year = bundle.getInt("Year");

        // ex: "SUMMER" -> "Summer"
        String title =
                season.substring(0,1).toUpperCase() + season.substring(1).toLowerCase() + " " + year;
        setTitle(title);

        String url = buildURL(season, year);

        new ParseURLTask().execute(url);
    }

    private void initGrid(Document doc) {
        Elements titles = doc.select("a.link-title");
        for (Element title : titles) {
            Log.d("gridUI", title.text());
        }
    }

    private String buildURL(String season, int year) {
        // URL FORMAT: PREFIX + SEASON (in lowercase) + '/' + YEAR
        StringBuilder url = new StringBuilder();
        url.append(MAL_PREFIX_URL);
        url.append(year);
        url.append('/');
        url.append(season.toLowerCase());

        return url.toString();
    }

    private Document getDocumentFromUrl(String url) {
        Document doc;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Could not make connection: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            return null;
        }
        return doc;
    }

    private class ParseURLTask extends AsyncTask<String, Void, Document> {
        String testUrl = "http://myanimelist.net/anime/season/2017/winter";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progessDialog = new ProgressDialog(AnimeCatalogActivity.this);
            progessDialog.setTitle("Fetching data");
            progessDialog.setMessage("Loading...");
            progessDialog.setIndeterminate(false);
            progessDialog.show();
        }

        @Override
        protected Document doInBackground(String... params) {
            Document doc = null;
            try {
                doc = Jsoup.connect(testUrl).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return doc;
        }

        @Override
        protected void onPostExecute(Document html) {
            Elements images = html.select("img");

            ArrayList<AnimeItem> animeItems = new ArrayList<AnimeItem>();

            for (Element image : images) {
                AnimeItem tempItem = new AnimeItem(image.attr("alt"));
                animeItems.add(tempItem);
//                animeTitles.add(image.attr("alt"));
            }

            final GridView animeListView = (GridView) findViewById(R.id.anime_catalog);
            animeListView.setAdapter(new AnimeListAdapter(getApplicationContext(), animeItems));
            // TODO: Parse other needed elements for AnimeItem and needed for this activity
            // For item: img url, synopsis, studio, epcount, genre(s)
            // For gridview: img url

            //Log.d("len", "SB: " + allTitles);
            progessDialog.dismiss();
        }
    }
}
