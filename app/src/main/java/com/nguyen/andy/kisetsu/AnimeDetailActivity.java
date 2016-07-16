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
            Elements images = html.select("img");
            Elements malUrls = html.select("a[class=box-unit7-btn di-box]"); //box-unit7-btn di-box

            Log.d("doc", "html file:\n" + html.toString());

            for (int i = 0; i < images.size() && i < malUrls.size(); i++) {
                String title  = images.get(i).attr("alt");
                String imgUrl = images.get(i).attr("data-src");
                String malUrl = malUrls.get(i).attr("href");
            }

            EditText et = (EditText) findViewById(R.id.edit_text);
            et.setText(html.toString());

            // TODO: images are 130x194 px

            progessDialog.dismiss();
        }
    }
}
