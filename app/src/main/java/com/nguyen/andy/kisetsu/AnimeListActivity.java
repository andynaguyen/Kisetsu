package com.nguyen.andy.kisetsu;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class AnimeListActivity extends AppCompatActivity {
    private static final String MAL_PREFIX_URL = "http://myanimelist.net/anime/season/";

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

        // URL FORMAT: PREFIX + SEASON (in lowercase) + '/' + YEAR
        StringBuilder url = new StringBuilder();
        url.append(MAL_PREFIX_URL);
        url.append(season.toLowerCase());
        url.append('/');
        url.append(year);


        /*Document doc = getDocumentFromUrl(url.toString());
        if (doc != null) {
            initGrid(doc);
        }*/
    }

    private void initGrid(Document doc) {
        Elements titles = doc.select("a.link-title");
        for (Element title : titles) {
            Log.d("gridUI", title.text());
        }
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
}
