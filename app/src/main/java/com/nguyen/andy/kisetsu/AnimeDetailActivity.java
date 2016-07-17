package com.nguyen.andy.kisetsu;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.app.ProgressDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class AnimeDetailActivity extends AppCompatActivity {
    private String malUrl;
    private String imgUrl;
    private String title;

    // needed for intent to go back to anime catalog
    private String seasonFrom;
    private int yearFrom;

    ProgressDialog progessDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_detail);

        Bundle bundle = getIntent().getExtras();
        malUrl = bundle.getString("MalUrl");
        imgUrl = bundle.getString("ImgUrl");
        title  = bundle.getString("Title");
        seasonFrom = bundle.getString("SeasonFrom");
        yearFrom = bundle.getInt("YearFrom");

        TextView titleView = (TextView) findViewById(R.id.detail_title);
        titleView.setText(malUrl);
        Log.d("url", "mal " + malUrl);
        titleView.setTextColor(Color.BLACK);

        new ParseURLTask().execute(malUrl);

        //TODO: Parse malurl for info
        // synopsis, rating, mediatype, studio, genre(s), epcount, air dates
        //TODO: Open in browser button on main bar
        //TODO: handle intent on back button pressed
    }

    private class ParseURLTask extends AsyncTask<String, Void, Document> {
        //String testUrl = "http://myanimelist.net/anime/season/2017/winter";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progessDialog = new ProgressDialog(AnimeDetailActivity.this);
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
            // Elements of html doc
            String synopsis = getSynopsis(html);
            String score = getScore(html);
            HashMap<String, String> info = getGeneralInfo(html);
            String type = info.get("Type");
            String studios = info.get("Studios");
            String genres = info.get("Genres");
            String episodes = info.get("Episodes");
            String aired = info.get("Aired");

            Log.d("TEST", synopsis);
            Log.d("TEST", score);
            Log.d("TEST", type);
            Log.d("TEST", studios);
            Log.d("TEST", genres);
            Log.d("TEST", episodes);
            Log.d("TEST", aired);

            // TODO: images are 130x194 px

            progessDialog.dismiss();
        }

        private String getSynopsis(Document doc) {
            Element synopsis = doc.select("meta[property=og:description]").first();

            if (synopsis != null) {
                return synopsis.attr("content");
            } else {
                return "ERROR FETCHING SYNOPSIS";
            }
        }

        private String getScore(Document doc) {
            Element reviewDetailTable = doc.select("table[class=review-detail-status]").first();

            if (reviewDetailTable != null) {
                Elements rows = reviewDetailTable.select("tr");
                if (rows != null) {
                    Element scoreRow = rows.get(1);
                    if (scoreRow != null) {
                        Elements tableData = scoreRow.select("td");
                        if (tableData.get(1) != null) {
                            return tableData.get(1).text();
                        }
                    }
                }
            }

            return "ERROR FETCHING SCORE";
        }

        private HashMap<String, String> getGeneralInfo(Document doc) {
            HashMap<String, String> info = new HashMap<String, String>();
            Element detailsTable = doc.select("div[class=Detail]").first();
            Elements detailRows = detailsTable.select("tr");

            for (Element tr : detailRows) {
                Elements td = tr.select("td");
                String key, val;
                if (td.get(0) == null) continue;

                key = td.get(0).text();

                if (td.get(1) != null) {
                    val = td.get(1).text();
                } else {
                    val = "ERROR FETCHING " + td.get(0);
                }

                info.put(key, val);
            }

            return info;
        }
    }
}
