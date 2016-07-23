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

import org.w3c.dom.Text;


public class AnimeDetailActivity extends AppCompatActivity {
    private static final int SYNOPSIS_PADDING_LEFT = 54;
    private static final int SYNOPSIS_PADDING_TOP = 30;
    private static final int SYNOPSIS_PADDING_RIGHT = 30;
    private static final int SYNOPSIS_PADDING_BOTTOM = 42;
    private static final int SYNOPSIS_TEXTSIZE = 18;
    private static final HashMap<String, String> RATING_MAP;

    static {
        RATING_MAP = new HashMap<String, String>();
        RATING_MAP.put("PG-13 - Teens 13 or older", "PG-13");
        RATING_MAP.put("R - 17+ (violence & profanity)", "R-17+");
        RATING_MAP.put("G - All Ages", "G");
    }

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
    }

    private class ParseURLTask extends AsyncTask<String, Void, DetailParser> {
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
            return new DetailParser(params[0]);
        }

        @Override
        protected void onPostExecute(DetailParser parser) {
            // get all fields needed to display
            String synopsis = parser.parseSynopsis();

            HashMap<String, String> info = parser.parseGeneralInfo();
            String score = info.get("Score");
            String ranked = info.get("Ranked");
            String popularity = info.get("Popularity");
            String type = info.get("Type");
            String status = info.get("Status");
            String aired = info.get("Aired");
            String rating = info.get("Rating");
            String episodes = info.get("Episodes");
            String duration = info.get("Duration");
            String source = info.get("Source");
            String studios = info.get("Studios");
            String genres = info.get("Genres");

            // set title to be "[__TYPE__] TITLE"
            setTitle("[" + type + "] " + title);
            AnimeDetailActivity.this.summary = synopsis;

            // modify the strings
            score = score.split("\\*")[0];
            ranked = ranked.split("\\*")[0];
            Log.d("popularity", "before " + popularity);
            rating = RATING_MAP.get(rating);
            Log.d("popularity", "after " + popularity);


            TextView titleTV = (TextView) findViewById(R.id.detail_title);
            titleTV.setText(title);
            TextView scoreTV = (TextView) findViewById(R.id.detail_score);
            scoreTV.setText(score);
            TextView rankedTV = (TextView) findViewById(R.id.detail_ranked);
            rankedTV.setText(ranked);
            TextView popularityTV = (TextView) findViewById(R.id.detail_popularity);
            popularityTV.setText(popularity);
            TextView typeTV = (TextView) findViewById(R.id.detail_media_type);
            typeTV.setText(type);
            TextView statusTV = (TextView) findViewById(R.id.detail_status);
            statusTV.setText(status);
            TextView airedTV = (TextView) findViewById(R.id.detail_aired_on);
            airedTV.setText(aired);
            TextView ratingTV = (TextView) findViewById(R.id.detail_rating);
            ratingTV.setText(rating);
            TextView episodesTV = (TextView) findViewById(R.id.detail_episodse);
            episodesTV.setText(episodes);
            TextView durationTV = (TextView) findViewById(R.id.detail_duration);
            durationTV.setText(duration);
            TextView sourceTV = (TextView) findViewById(R.id.detail_source);
            sourceTV.setText(source);
            TextView studiosTV = (TextView) findViewById(R.id.detail_studios);
            studiosTV.setText(studios);
            TextView genresTV = (TextView) findViewById(R.id.detail_genres);
            genresTV.setText(genres);

            progessDialog.dismiss();
        }
    }
}
