package com.nguyen.andy.kisetsu;

import android.app.ActionBar;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import com.nguyen.andy.kisetsu.parsers.DetailParser;

public class AnimeDetailActivity extends AppCompatActivity {
    // constants
    private static final int SYNOPSIS_PADDING_LEFT = 54;
    private static final int SYNOPSIS_PADDING_TOP = 30;
    private static final int SYNOPSIS_PADDING_RIGHT = 36;
    private static final int SYNOPSIS_PADDING_BOTTOM = 42;
    private static final int SYNOPSIS_TEXTSIZE = 18;
    private static final HashMap<String, String> RATING_MAP; // simplifies the parsed Ratings

    static {
        RATING_MAP = new HashMap<String, String>();
        RATING_MAP.put("R - 17+ (violence & profanity)", "R-17+");
        RATING_MAP.put("R+ - Mild Nudity", "R-17+");
        RATING_MAP.put("PG-13 - Teens 13 or older", "PG-13");
        RATING_MAP.put("G - All Ages", "G");
        RATING_MAP.put("None", "None");
    }

    // fields
    private String malUrl;
    private String imgUrl;
    private String title;
    private String summary;
    private String seasonFrom;
    private int yearFrom;
    private boolean isFirstVisible = true;  // for ViewFlipper

    ProgressDialog progessDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_detail);

        // unpack variables from intent
        Bundle bundle = getIntent().getExtras();
        malUrl = bundle.getString("MalUrl");
        imgUrl = bundle.getString("ImgUrl");
        title  = bundle.getString("Title");
        seasonFrom = bundle.getString("SeasonFrom");
        yearFrom = bundle.getInt("YearFrom");

        setTitle("[  ] " + title);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        /* Removed FAB for a more traditional button
        // make FAB
        FloatingActionButton detailFab = (FloatingActionButton) findViewById(R.id.detail_fab);
        detailFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(malUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }); */

        // Create MAL button
        TextView malButton = (TextView) findViewById(R.id.mal_button);
        malButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(malUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        // if connected to internet, start web scraping.
        // else, make toast
        if (ConnectivityCheck.isConnectedToInternet(getApplicationContext())) {
            new ParseURLTask().execute(malUrl);
        } else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_mal_link:
                Uri uri = Uri.parse(malUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // asyncTask dedicated to parsing the HTML
    private class ParseURLTask extends AsyncTask<String, Void, DetailParser> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // start progressDialog
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
            rating = RATING_MAP.get(rating);

            // set text for all the views
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

            // load image for thumbnail and set onclicklistener
            ImageView thumbnailView= (ImageView) findViewById(R.id.thumbnail);
            Picasso.with(getApplicationContext())
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

                    // left 60, top 30 , right 36, bottom 48
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

            // ViewFlipper
            ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper);
            RelativeLayout scoreLayout = (RelativeLayout) findViewById(R.id.score_stats_layout);
            RelativeLayout otherLayout = (RelativeLayout) findViewById(R.id.other_stats_layout);

            scoreLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNext();
                }
            });

            otherLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNext();
                }
            });

            progessDialog.dismiss();
        }

        private void showNext() {
            ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper);
            flipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left));
            flipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right));
            flipper.showNext();
        }

        private void flip() {
            if (isFirstVisible) {
                isFirstVisible = false;
            } else {
                isFirstVisible = true;
            }
            ((ViewFlipper) findViewById(R.id.flipper)).showNext();
        }
    }
}