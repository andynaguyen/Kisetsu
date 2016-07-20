package com.nguyen.andy.kisetsu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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

    ProgressDialog progessDialog;

    // fields for Intent
    String season;
    int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_catalog);

        Bundle bundle = getIntent().getExtras();
        season = bundle.getString("Season");
        year = bundle.getInt("Year");

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
                doc = Jsoup.connect(params[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return doc;
        }

        @Override
        protected void onPostExecute(Document html) {
            final ArrayList<AnimeItem> animeItems = new ArrayList<AnimeItem>();

            Elements titles = html.select("div[class=title]");
            Elements malUrls = html.select("a[class=thumb]");
            Elements imgUrls = html.select("div[style=\"\"]");

            for (int i = 0; i < titles.size() && i < malUrls.size() && i < imgUrls.size(); i++) {
                String title, imgUrl, malUrl;



                if (titles.get(i) != null) {
                    title = titles.get(i).text();
                } else {
                    title = "ERROR GETTING TITLE";
                }

                if (malUrls.get(i) != null) {
                    malUrl = malUrls.get(i).attr("href");
                } else {
                    malUrl = "ERROR GETTING URL";
                }

                if (imgUrls.get(i) != null) {
                    imgUrl = imgUrls.get(i).attr("data-bg");
                } else {
                    imgUrl = "ERROR GETTING IMAGE";
                }

                animeItems.add(new AnimeItem(title, imgUrl, malUrl));
            }

            /* BEFORE THE MAL MOBILE UPDATE
            // Elements of html doc
            Elements images = html.select("img");
            Elements malUrls = html.select("a[class=box-unit7-btn di-box]"); //box-unit7-btn di-box

            Log.d("images", ""+images.size());
            Log.d("malUrls", "" + malUrls.size());

            for (int i = 0; i < images.size() && i < malUrls.size(); i++) {
                String title, imgUrl, malUrl;
                if (images.get(i) != null) {
                    title = images.get(i).attr("alt");
                    imgUrl = images.get(i).attr("data-src");
                } else {
                    title = "ERROR GETTING TITLE";
                    imgUrl = "ERROR GETTING IMAGE";
                }

                if (malUrls.get(i) != null) {
                    malUrl = malUrls.get(i).attr("href");
                } else {
                    malUrl = "ERROR GETTING LINK";
                }

                Log.d("debug", "Title[" + i + "]: " + title);
                Log.d("debug", "imgUrl[" + i + "]: " + imgUrl);
                Log.d("debug", "malUrl[" + i + "]: " + malUrl);

                animeItems.add(new AnimeItem(title, imgUrl, malUrl));
            } */

            final GridView animeGridView = (GridView) findViewById(R.id.anime_catalog);
            animeGridView.setAdapter(new AnimeListAdapter(getApplicationContext(), animeItems));
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
            // TODO: images are 130x194 px

            progessDialog.dismiss();
        }
    }
}
