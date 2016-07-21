package com.nguyen.andy.kisetsu;

import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.ProgressDialog;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import com.nguyen.andy.kisetsu.parsers.DetailParser;


public class AnimeDetailActivity extends AppCompatActivity {
    private static final int SYNOPSIS_PADDING_LEFT = 54;
    private static final int SYNOPSIS_PADDING_TOP = 30;
    private static final int SYNOPSIS_PADDING_RIGHT = 30;
    private static final int SYNOPSIS_PADDING_BOTTOM = 42;
    private static final int SYNOPSIS_TEXTSIZE = 18;

    private String malUrl;
    private String imgUrl;
    private String title;
    private String summary;

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

        setTitle("[  ] " + title);

        // FAB
        FloatingActionButton detailFab = (FloatingActionButton) findViewById(R.id.detail_fab);
        detailFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(malUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Log.d("malurl", malUrl);

        new ParseURLTask().execute(malUrl);

        // load image for thumbnail and set onclicklistener
        ImageView thumbnailView= (ImageView) findViewById(R.id.thumbnail);
        Log.d("thumbnail", "url: " + imgUrl);
        Picasso.with(this)
                .load(imgUrl)
                .fit()
                .into(thumbnailView);
        thumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder popupBuilder = new AlertDialog.Builder(AnimeDetailActivity.this);
                TextView tmp = new TextView(AnimeDetailActivity.this);
                tmp.setText(summary);
                tmp.setTextSize(SYNOPSIS_TEXTSIZE);

                // left 60, top 30 , right 30, bottom 48
                tmp.setPadding(
                        SYNOPSIS_PADDING_LEFT,
                        SYNOPSIS_PADDING_TOP,
                        SYNOPSIS_PADDING_RIGHT,
                        SYNOPSIS_PADDING_BOTTOM
                );

                popupBuilder.setView(tmp);
                popupBuilder.show();
            }
        });

        // set onclicklistener for TAP FOR SUMMARY
        TextView tapForSummaryView = (TextView) findViewById(R.id.detail_tap_for_summary);
        tapForSummaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder popupBuilder = new AlertDialog.Builder(AnimeDetailActivity.this);
                TextView tmp = new TextView(AnimeDetailActivity.this);
                tmp.setText(summary);
                tmp.setTextSize(SYNOPSIS_TEXTSIZE);

                // left 60, top 30 , right 30, bottom 48
                tmp.setPadding(
                        SYNOPSIS_PADDING_LEFT,
                        SYNOPSIS_PADDING_TOP,
                        SYNOPSIS_PADDING_RIGHT,
                        SYNOPSIS_PADDING_BOTTOM
                );

                popupBuilder.setView(tmp);
                popupBuilder.show();
            }
        });

        //TODO: Open in browser button on main bar
        //TODO: handle intent on back button pressed
    }

    private class ParseURLTask extends AsyncTask<String, Void, DetailParser> {
        //String testUrl = "http://myanimelist.net/anime/season/2017/winter";
        //DetailParser parser;

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
        protected DetailParser doInBackground(String... params) {
            /*Document doc = null;
            try {
                doc = Jsoup.connect(params[0]).get();
                parser = new DetailParser(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            DetailParser parser = new DetailParser(params[0]);

            if (parser == null) return null;

            return parser;
        }

        @Override
        protected void onPostExecute(DetailParser parser) {
            // get all fields needed to display
            String synopsis = parser.parseSynopsis();
            String score = parser.parseScore();

            HashMap<String, String> info = parser.parseGeneralInfo();
            String type = info.get("Type");
            String studios = info.get("Studios");
            String genres = info.get("Genres");
            String episodes = info.get("Episodes");
            String aired = info.get("Aired");
            String status = info.get("Status");

            // set title to be "[__TYPE__] TITLE"
            setTitle("[" + type + "] " + title);
            AnimeDetailActivity.this.summary = synopsis;

            progessDialog.dismiss();
        }

        /*
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

                //Log.d("HashMap", "k: " + key + ", val: " + val);

                info.put(key, val);
            }

            return info;
        }*/
    }
}
