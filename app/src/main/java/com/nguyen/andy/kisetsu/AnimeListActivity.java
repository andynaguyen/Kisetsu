package com.nguyen.andy.kisetsu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class AnimeListActivity extends AppCompatActivity {
    private static final String MAL_PREFIX_URL = "http://myanimelist.net/anime/season/";
    //ArrayList<String> titles;
    ProgressDialog progessDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_list);

        Bundle bundle = getIntent().getExtras();
        String season = bundle.getString("Season");
        int year = bundle.getInt("Year");

        // ex: "SUMMER" -> "Summer"
        String title =
                season.substring(0,1).toUpperCase() + season.substring(1).toLowerCase() + " " + year;
        setTitle(title);

        String url = buildURL(season, year);
        //Log.d("url", url);

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

            String allTitles = "";

            for (Element image : images) {
                allTitles += image.attr("alt") + "\n";
            }

            Log.d("len", "SB: " + allTitles);
            progessDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progessDialog = new ProgressDialog(AnimeListActivity.this);
            progessDialog.setTitle("Fetching data");
            progessDialog.setMessage("Loading...");
            progessDialog.setIndeterminate(false);
            progessDialog.show();
        }
    }
}
